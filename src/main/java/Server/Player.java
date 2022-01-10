package Server;

import Client.DataPackage;

import java.io.*;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.ArrayList;

public class Player extends Thread {

    private static BufferedReader input;
    private static PrintWriter output;
    private static ObjectOutputStream packageSender;
    private static ObjectInputStream packageReader;
    private final Socket client;
    DataPackage dataPackage;

    private ArrayList<Player> players;


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
                dataPackage = (DataPackage) packageReader.readObject();
                String command = dataPackage.getClientCommand();
                switch(command) {

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                packageReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                packageSender.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void startGame(ArrayList<Player> players) throws IOException {
        this.players = players;
        input.close();
        output.close();
        packageReader = new ObjectInputStream(client.getInputStream());
        packageSender = new ObjectOutputStream(client.getOutputStream());
        start();
    }

    public Socket getSocket() {
        return client;
    }
}
