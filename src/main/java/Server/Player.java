package Server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;

public class Player extends Thread{
    private String serverMessage;
    private Socket client;
    private static DataInputStream input;
    private static DataOutputStream output;
    public Player(Socket socket) throws IOException{
        this.client=socket;
        input = new DataInputStream(client.getInputStream());
        output = new DataOutputStream(client.getOutputStream());
    }
    @Override
    public void run(){
        try {
            while(client.isConnected()) {
                String command = input.readUTF();
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
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void sendMessage(String message) throws IOException {
        output.writeUTF(message);
    }
    public String getServerMessage() throws IOException {
            serverMessage = input.readUTF();
            return serverMessage;
    }
}
