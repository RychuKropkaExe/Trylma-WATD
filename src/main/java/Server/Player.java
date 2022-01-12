package Server;

import Client.DataPackage;
import Client.Pakiet;
import Client.Pawn;
import Client.Tile;
import Server.Rules.Rules;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.ArrayList;

public class Player extends Thread {
    private final ObjectOutputStream packageSender;
    private final ObjectInputStream packageReader;
    private final Socket client;
    DataPackage dataPackage;

    private ArrayList<Tile> clientTiles;
    private ArrayList<Pawn> clientPawns;
    private ArrayList<Pawn> clientMovablePawns;
    private ArrayList<Point> clientWinPoints;
    private ArrayList<Player> players;
    private Rules rules;


    public Player(Socket socket, Rules rules) throws IOException{
        this.client = socket;
        this.rules = rules;
        packageSender = new ObjectOutputStream(socket.getOutputStream());
        packageReader = new ObjectInputStream(socket.getInputStream());
        /*packageSender = new ObjectOutputStream(client.getOutputStream());
        packageReader = new ObjectInputStream(client.getInputStream());*/
        System.out.println(client);
    }

    @Override
    public void run() {
        try {
            while(client.isConnected()) {
                dataPackage = (DataPackage) packageReader.readObject();
                String command = dataPackage.getClientCommand();
                if(command.equals("Validate")) {
                    if(rules.checkMove(dataPackage)) {
                    /*clientPawns = dataPackage.getClientPawns();
                    clientTiles = dataPackage.getClientTiles();
                    clientMovablePawns = dataPackage.getClientMovablePawns();
                    clientWinPoints = dataPackage.getWinPoints();*/
                        dataPackage.setServerResponse("Valid");
                        packageSender.reset();
                        packageSender.writeObject(dataPackage);
                        packageSender.flush();
                    } else {
                        dataPackage.setServerResponse("Invalid");
                        packageSender.reset();
                        packageSender.writeObject(dataPackage);
                        packageSender.flush();
                    }
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

    public void startGame(ArrayList<Player> players) throws IOException {
        this.players = players;
        this.start();
    }

    public Socket getSocket() {
        return client;
    }
    public ObjectOutputStream getOutput() {
        return packageSender;
    }
    public void sendMessage(String message) throws IOException {
        Pakiet pakiet = new Pakiet(message);
        packageSender.reset();
        packageSender.writeObject(pakiet);
        packageSender.flush();
    }
    public void sendMessage(int number) throws IOException, ClassNotFoundException {
        Pakiet pakiet = new Pakiet(number);
        packageSender.reset();
        packageSender.writeObject(pakiet);
        packageSender.flush();
    }
    public Pakiet getMessage() throws IOException, ClassNotFoundException {
        return (Pakiet)packageReader.readObject();
    }
}
