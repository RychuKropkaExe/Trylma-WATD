package Server;

import java.io.*;
import java.net.Socket;

public class Player implements Runnable{
    private String serverMessage;
    private Socket client;
    private static BufferedReader input;
    private static PrintWriter output;
    public Player(Socket socket) throws IOException{
        this.client=socket;
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(client.getOutputStream());
    }
    @Override
    public void run(){
        try {
            while(client.isConnected()) {
                String command = input.readLine();
                switch(command) {
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            output.close();
        }

    }
    public void sendMessage(String message) {
        output.println(message);
        output.flush();
    }
    public String getServerMessage() throws IOException {
        while(true) {
            if((serverMessage = input.readLine())!=null) {
                return serverMessage;
            }
        }
    }
}
