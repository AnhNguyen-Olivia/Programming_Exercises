package tictactoe_new;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException{
        try(Socket socket = new Socket("localhost", 9000)){
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

                    if (line.contains("wins!") || line.contains("Draw!")){
                        System.out.println("Game over. Adieu!");
                        return;
                    }
                }

                // write to server
                String respone = userInput.readLine();
                out.println(respone);
            }
        }
    }
}
