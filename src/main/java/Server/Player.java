package Server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;

public class Player extends Thread {

    private static BufferedReader input;
    private static PrintWriter output;
    private final Socket client;


    public Player(Socket socket) throws IOException{
        this.client = socket;
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));

        System.out.println(client);
    }

    @Override
    public void run() {
        try {
            while(client.isConnected()) {
                String command = input.readLine();
                switch(command) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            output.close();
        }
    }

    public void sendMessage(String message) {
        output.print(message);
        output.print("\n");
        output.flush();
    }

    public String getServerMessage() throws IOException {
        return input.readLine();
    }

    public Socket getSocket() {
        return client;
    }
}
