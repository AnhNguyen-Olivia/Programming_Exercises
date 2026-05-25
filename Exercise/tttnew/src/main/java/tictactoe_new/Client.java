package tictactoe_new;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static volatile boolean running = true;
    private static final LinkedBlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        String hostname = SERVER_ADDRESS;
        int port = SERVER_PORT;

        if (args.length >= 1) hostname = args[0];
        if (args.length >= 2) port = Integer.parseInt(args[1]);

        // Daemon thread: reads stdin continuously into the queue.
        // Nothing is consumed until the server explicitly asks for input.
        Thread stdinReader = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                String line;
                while (running && (line = br.readLine()) != null) {
                    inputQueue.put(line);
                }
            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        stdinReader.setDaemon(true);
        stdinReader.start();

        try (Socket socket = new Socket(hostname, port);
             BufferedReader serverInput = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to server at " + hostname + ":" + port + "\n");

            while (running) {
                String serverMessage = serverInput.readLine();
                if (serverMessage == null) { running = false; break; }

                boolean needsInput = false;
                System.out.println(serverMessage);
                if (requiresUserInput(serverMessage)) needsInput = true;

                // Drain any buffered server messages
                while (serverInput.ready()) {
                    String extra = serverInput.readLine();
                    if (extra == null) { running = false; break; }
                    System.out.println(extra);
                    if (requiresUserInput(extra)) needsInput = true;
                }

                if (!running) break;

                if (needsInput) {
                    // KEY FIX: discard everything typed before this prompt arrived.
                    inputQueue.clear();

                    // Now wait for one fresh line typed after the prompt.
                    String line = null;
                    while (running && (line == null || line.trim().isEmpty())) {
                        try {
                            line = inputQueue.poll(100, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            running = false;
                            break;
                        }
                    }

                    if (!running || line == null) break;

                    serverOutput.println(line);

                    if (line.equalsIgnoreCase("quit")) {
                        running = false;
                        break;
                    }
                }
                // If no input needed, we simply loop — anything in the queue
                // will be cleared the next time the server asks.
            }

        } catch (ConnectException e) {
            System.err.println("Error: Could not connect to server at " + hostname + ":" + port);
            System.err.println("Make sure the server is running first.");
        } catch (SocketException e) {
            if (running) System.err.println("Connection error: " + e.getMessage());
        } catch (IOException e) {
            if (running) System.err.println("I/O error: " + e.getMessage());
        }

        running = false;
        System.out.println("Disconnected from server.");
    }

    private static boolean requiresUserInput(String serverMessage) {
        String msg = serverMessage.toLowerCase();
        return msg.startsWith("your turn")
                || msg.contains("enter cell number")
                || msg.contains("invalid input")
                || msg.contains("invalid cell number")
                || msg.contains("the cell is occupied")
                || msg.contains("game is over")
                || msg.contains("game finished")
                || msg.contains("type 'reset'");
    }
}