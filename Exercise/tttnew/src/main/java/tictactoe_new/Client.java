package tictactoe_new;

import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) throws IOException {
        String hostname = SERVER_ADDRESS;
        int port = SERVER_PORT;

        if (args.length >= 1) {
            hostname = args[0];
        }

        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }

        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName(hostname);

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Connected to server at " + hostname + ":" + port);
        System.out.println("Enter cell number (1-9), 'board' to view, or 'reset' for new game");
        System.out.println("Commands: 'quit' to exit\n");

        byte[] receiveBuffer = new byte[1024];

        while (true) {
            System.out.print("Your move: ");
            String userInput = stdIn.readLine();

            if (userInput.equalsIgnoreCase("quit")) {
                break;
            }

            byte[] sendBuffer = userInput.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                    sendBuffer, sendBuffer.length,
                    serverAddress, port);
            socket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(response);

            if (response.contains("won!") || response.contains("draw!")) {
                System.out.print("\nPlay again? (yes/no): ");
                String playAgain = stdIn.readLine();
                if (playAgain.equalsIgnoreCase("yes")) {
                    byte[] resetBuffer = "reset".getBytes();
                    DatagramPacket resetPacket = new DatagramPacket(
                            resetBuffer, resetBuffer.length,
                            serverAddress, port);
                    socket.send(resetPacket);

                    DatagramPacket resetResponse = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    socket.receive(resetResponse);
                    System.out.println(new String(resetResponse.getData(), 0, resetResponse.getLength()));
                } else {
                    break;
                }
            }
        }

        socket.close();
        System.out.println("Disconnected from server.");
    }
}