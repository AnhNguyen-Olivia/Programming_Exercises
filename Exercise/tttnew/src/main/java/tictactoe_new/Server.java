package tictactoe_new;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private GameHost gameHost;
    private Queue<ClientConnection> waitingClients;
    private ClientConnection currentPlayer;
    private Set<ClientConnection> connectedClients;

    private static class ClientConnection {
        private final Socket socket;
        private final BufferedReader input;
        private final PrintWriter output;

        public ClientConnection(Socket socket) throws IOException {
            this.socket = socket;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream(), true);
        }

        public String readLine() throws IOException {
            return input.readLine();
        }

        public void sendMessage(String message) {
            output.println(message);
        }

        public void close() throws IOException {
            input.close();
            output.close();
            socket.close();
        }

        public String getRemoteAddress() throws IOException {
            return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    public Server() {
        this.gameHost = new GameHost();
        this.waitingClients = new LinkedList<>();
        this.connectedClients = new HashSet<>();
        this.currentPlayer = null;
    }

    private void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server listening on port " + PORT);
        System.out.println("Waiting for client connections...\n");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            handleNewConnection(clientSocket);
        }
    }

    private void handleNewConnection(Socket clientSocket) throws IOException {
        ClientConnection client = new ClientConnection(clientSocket);
        connectedClients.add(client);

        String welcomeMsg;
        if (currentPlayer == null) {
            gameHost.reset();
            currentPlayer = client;
            welcomeMsg = "Welcome! You are now playing.\n" + gameHost.getBoardState()
                    + "\nYour turn. Enter cell number (1-9), 'board' to view, 'reset' for new game, or 'quit' to exit.\n";
            System.out.println("Client " + client.getRemoteAddress() + " is now playing.");
        } else {
            waitingClients.offer(client);
            welcomeMsg = "Welcome! A game is currently in progress.\nPlease wait for your turn...\n";
            System.out.println("Client " + client.getRemoteAddress() + " is waiting. Queue size: " + waitingClients.size());
        }

        client.sendMessage(welcomeMsg);
        handleClient(client);
    }

    private void handleClient(ClientConnection client) {
        new Thread(() -> {
            try {
                while (true) {
                    String message = client.readLine();
                    if (message == null) {
                        handleClientDisconnect(client);
                        return;
                    }

                    message = message.trim();
                    if (message.isEmpty()) {
                        continue;
                    }

                    if (message.equalsIgnoreCase("quit")) {
                        handleClientDisconnect(client);
                        return;
                    }

                    if (currentPlayer == client) {
                        GameHost.MoveResult result = gameHost.processMove(message);
                        client.sendMessage(result.getResponse());

                        if (result.isFinishedGame() && !waitingClients.isEmpty()) {
                            client.sendMessage("Game finished. Switching to next waiting player...\n");
                            nextPlayerTurn();
                        }
                    } else if (waitingClients.contains(client)) {
                        client.sendMessage("Still waiting... Current player is playing.\n");
                    } else {
                        client.sendMessage("Please wait. Another player is currently playing.\n");
                    }

                    System.out.println("Message from " + client.getRemoteAddress() + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("I/O error with client: " + e.getMessage());
                try {
                    handleClientDisconnect(client);
                } catch (IOException ex) {
                    System.out.println("Error disconnecting client: " + ex.getMessage());
                }
            }
        }).start();
    }

    private synchronized void nextPlayerTurn() throws IOException {
        if (!waitingClients.isEmpty()) {
            currentPlayer = waitingClients.poll();
            gameHost.reset();
            String msg = "Your turn now! Game reset.\n" + gameHost.getBoardState() + "\nYour turn\n";
            currentPlayer.sendMessage(msg);
            System.out.println("Next player started. Queue size: " + waitingClients.size());
        } else {
            currentPlayer = null;
            System.out.println("No more players waiting. Server ready for new connection.");
        }
    }

    private synchronized void handleClientDisconnect(ClientConnection client) throws IOException {
        connectedClients.remove(client);
        System.out.println("Client " + client.getRemoteAddress() + " disconnected.");

        if (currentPlayer == client) {
            System.out.println("Current player disconnected. Moving to next player...");
            currentPlayer = null;
            nextPlayerTurn();
        } else {
            waitingClients.remove(client);
            System.out.println("Waiting client removed. Queue size: " + waitingClients.size());
        }

        client.close();
    }
}
