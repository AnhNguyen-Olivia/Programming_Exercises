package tictactoe_new;

import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        String hostname = SERVER_ADDRESS;
        int port = SERVER_PORT;

        if (args.length >= 1) hostname = args[0];
        if (args.length >= 2) port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(hostname, port);
             BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server at " + hostname + ":" + port + "\n");

            boolean running = true;
            while (running) {
                String serverMessage = serverInput.readLine();
                if (serverMessage == null) break;

                boolean needsInput = false;
                System.out.println(serverMessage);
                if (requiresUserInput(serverMessage)) needsInput = true;

                while (serverInput.ready()) {
                    String extra = serverInput.readLine();
                    if (extra == null) { running = false; break; }
                    System.out.println(extra);
                    if (requiresUserInput(extra)) needsInput = true;
                }

                if (!running) break;

                if (needsInput) {
                    // Discard any buffered bytes typed earlier
                    discardBufferedStdin();

                    String line = userInput.readLine();
                    if (line == null) break;

                    serverOutput.println(line);
                    if (line.equalsIgnoreCase("quit")) break;

                    // After sending one command, discard any extra pasted lines
                    discardBufferedStdin();
                }
            }

        } catch (ConnectException e) {
            System.err.println("Error: Could not connect to server at " + hostname + ":" + port);
            System.err.println("Make sure the server is running first.");
        } catch (SocketException e) {
            System.err.println("Connection error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }

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

    private static void discardBufferedStdin() {
        try {
            InputStream in = System.in;
            while (in.available() > 0) {
                in.read();
            }
        } catch (IOException ignored) {
        }
    }
}
