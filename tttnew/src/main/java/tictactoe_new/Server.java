package tictactoe_new;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private final GameHost gameHost;
    private final Queue<SocketChannel> waitingClients;
    private SocketChannel currentPlayer;
    private final Map<SocketChannel, ClientState> clients;
    private Selector selector;

    private static class ClientState {
        StringBuilder inBuffer = new StringBuilder();
        Queue<String> outQueue = new LinkedList<>();
        ByteBuffer pendingWrite;
    }

    public static void main(String[] args) throws IOException {
        new Server().start();
    }

    public Server() {
        this.gameHost = new GameHost();
        this.waitingClients = new LinkedList<>();
        this.clients = new HashMap<>();
        this.currentPlayer = null;
    }

    private void start() throws IOException {
        try (Selector selector = Selector.open();
            ServerSocketChannel serverSock = ServerSocketChannel.open()) {

            serverSock.bind(new InetSocketAddress(PORT));
            serverSock.configureBlocking(false);
            serverSock.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server listening on port " + PORT);
            System.out.println("Waiting for client connections...\n");

            this.selector = selector;
            while (true) {
                selector.select();

                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    if (!key.isValid()) continue;

                    if (key.isAcceptable()) {
                        SocketChannel client = serverSock.accept();
                        if (client != null) {
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                            ClientState state = new ClientState();
                            clients.put(client, state);
                            handleNewConnection(client);
                        }
                    }

                    if (key.isValid() && key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        boolean closed = !readFromClient(client);
                        if (closed) {
                            disconnectClient(client, selector);
                            continue;  // key is cancelled — skip isWritable
                        }
                    }

                    if (key.isValid() && key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ClientState state = clients.get(client);
                        try {
                            if (state.pendingWrite != null) {
                                client.write(state.pendingWrite);
                                if (!state.pendingWrite.hasRemaining()) state.pendingWrite = null;
                            }

                            while (state.pendingWrite == null && !state.outQueue.isEmpty()) {
                                String msg = state.outQueue.poll();
                                ByteBuffer buf = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                                client.write(buf);
                                if (buf.hasRemaining()) {
                                    state.pendingWrite = buf;
                                    break;
                                }
                            }

                            if (state.pendingWrite == null && state.outQueue.isEmpty()) {
                                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                            }
                        } catch (IOException e) {
                            disconnectClient(client, selector);
                        }
                    }
                }
            }
        }
    }

    private void handleNewConnection(SocketChannel client) throws IOException {
        String addr = client.getRemoteAddress().toString();
        String welcome;
        if (currentPlayer == null) {
            gameHost.reset();
            currentPlayer = client;
            welcome = "Welcome! You are now playing.\n" + gameHost.getBoardState()
                    + "\nYour turn. Enter cell number (1-9), 'board' to view, 'reset' for new game, or 'quit' to exit.\n";
            System.out.println("Client " + addr + " is now playing.");
        } else {
            waitingClients.offer(client);
            welcome = "Welcome! A game is currently in progress.\nPlease wait for your turn...\n";
            System.out.println("Client " + addr + " is waiting. Queue size: " + waitingClients.size());
        }

        enqueueMessage(client, welcome);
    }

    private boolean readFromClient(SocketChannel client) {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            int read = client.read(buf);
            if (read == -1) return false;
            if (read == 0) return true;
            buf.flip();
            String s = StandardCharsets.UTF_8.decode(buf).toString();
            ClientState state = clients.get(client);
            state.inBuffer.append(s);

            String accumulated = state.inBuffer.toString();
            int idx;
            while ((idx = accumulated.indexOf('\n')) != -1) {
                String line = accumulated.substring(0, idx).trim();
                accumulated = accumulated.substring(idx + 1);
                if (!line.isEmpty()) handleClientLine(client, line);
            }
            state.inBuffer.setLength(0);
            state.inBuffer.append(accumulated);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void handleClientLine(SocketChannel client, String line) {
        try {
            String addr = client.getRemoteAddress().toString();

            if (line.equalsIgnoreCase("quit")) {
                System.out.println("Client " + addr + " requested quit.");
                disconnectClient(client, selector);  // promotes next player if needed
                return;
            }

            if (currentPlayer != null && currentPlayer.equals(client)) {
                GameHost.MoveResult result = gameHost.processMove(line);
                enqueueMessage(client, result.getResponse());

                if (result.isFinishedGame()) {
                    if (!waitingClients.isEmpty()) {
                        enqueueMessage(client, "Game finished. Switching to next waiting player...\n");
                        promoteNextPlayer();
                    }
                }
            } else if (waitingClients.contains(client)) {
                enqueueMessage(client, "Still waiting... Current player is playing.\n");
            } else {
                enqueueMessage(client, "Please wait. Another player is currently playing.\n");
            }

            System.out.println("Message from " + addr + ": " + line);
        } catch (IOException e) {
            // ignore
        }
    }

    private void promoteNextPlayer() throws IOException {
        if (!waitingClients.isEmpty()) {
            SocketChannel next = waitingClients.poll();
            currentPlayer = next;
            gameHost.reset();
            String msg = "Your turn now! Game reset.\n" + gameHost.getBoardState() + "\nYour turn\n";
            enqueueMessage(next, msg);
            System.out.println("Next player started. Queue size: " + waitingClients.size());
        } else {
            currentPlayer = null;
            System.out.println("No more players waiting. Server ready for new connection.");
        }
    }

    private void disconnectClient(SocketChannel client, Selector selector) {
        try {
            String addr = client.getRemoteAddress().toString();
            clients.remove(client);
            waitingClients.remove(client);
            if (currentPlayer != null && currentPlayer.equals(client)) {
                currentPlayer = null;
                System.out.println("Current player disconnected. Moving to next player...");
                promoteNextPlayer();
            }
            client.close();
            System.out.println("Client " + addr + " disconnected.");
        } catch (IOException e) {
            // ignore
        }
    }

    private void enqueueMessage(SocketChannel client, String message) {
        ClientState state = clients.get(client);
        if (state == null) return;
        if (!message.endsWith("\n")) message = message + "\n";
        state.outQueue.offer(message);
        SelectionKey key = client.keyFor(selector);
        if (key != null) {
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        }
    }
}
