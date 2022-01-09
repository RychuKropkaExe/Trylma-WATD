package Server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;

public class Player extends Thread{
    private volatile String serverMessage;
    private Socket client;
    private static BufferedReader input;
    private static PrintWriter output;
    public Player(Socket socket) throws IOException{
        this.client=socket;
        System.out.println(client);
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));
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
    public void sendMessage(String message) throws IOException {
        output.print(message);
        output.print("\n");
        output.flush();
    }
    public String getServerMessage() throws IOException {
            serverMessage = input.readLine();
            return serverMessage;
    }
    public Socket getSocket() {
        return client;
    }
}
