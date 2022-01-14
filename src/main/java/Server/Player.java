package Server;

import Client.DataPackage;
import Client.Pakiet;
import Client.Pawn;
import Client.Tile;
import Server.Rules.Rules;

import javax.xml.crypto.Data;
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
    private final Rules rules;
    private int[][] corners;


    public Player(Socket socket, Rules rules) throws IOException{
        this.client = socket;
        this.rules = rules;
        this.corners=corners;
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
                        if(rules.stillMove()) {
                            dataPackage.setServerResponse("Valid & move");
                            packageSender.reset();
                            packageSender.writeObject(dataPackage);
                            packageSender.flush();
                            updatePlayers(dataPackage);
                        } else {
                            dataPackage.setServerResponse("Valid");
                            dataPackage.setCurrentPlayer(nextPlayer(dataPackage.getCurrentPlayer()));
                            packageSender.reset();
                            packageSender.writeObject(dataPackage);
                            packageSender.flush();
                            updatePlayers(dataPackage);

                        }
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
    private int nextPlayer(int player) {
        int current = 0;
        for(int i = 0; i<players.size(); i++) {
            if(corners[i][0]==player) {
                current=i;
                break;
            }
        }
        for(int i=0;i<players.size(); i++) {
            if(corners[i][0]==player) {
                if(current+1==players.size()) {
                    return corners[0][0];
                } else {
                    return corners[current+1][0];
                }
            }
        }
        return corners[0][0];
    }

    public void startGame(ArrayList<Player> players,int[][] corners) throws IOException {
        this.corners=corners;
        this.players = players;
        this.start();
    }

    private void updatePlayers(DataPackage dataPackage) throws IOException {
        DataPackage temp = dataPackage;
        for(Player player : players) {
            if(!player.equals(this)) {
                temp.setServerResponse("Update");
                player.getOutput().reset();
                player.getOutput().writeObject(temp);
                player.getOutput().flush();
            }
        }


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
