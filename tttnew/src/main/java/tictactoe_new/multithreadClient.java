package tictactoe_new;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * This is the client to test the multhread server. Nothing change other than the port lol =)))
 */
public class multithreadClient {
    /**
     * Main method to start the client and connect to the server.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{
        try(Socket socket = new Socket("localhost", 9020)){
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream(), true);
            
            // for userInput
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String greeting = in.readLine();
            System.out.println(greeting);
            out.println("1");

            while (true){

                // read from server
                String line;
                while((line = in.readLine()) != null){
                    System.out.println(line);
                    if (line.contains("Enter cell")) break;

                    if (line.contains("wins!") || line.contains("Draw!") || line.contains("Game quit by user. Adieu!")){
                        return;
                    }
                }

                // write to server
                String response = userInput.readLine();
                out.println(response);
            }
        }
    }
}
