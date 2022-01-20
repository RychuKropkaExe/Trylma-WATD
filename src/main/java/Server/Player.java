package Server;

import Client.*;
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
    private ArrayList<Player> players;
    private final Rules rules;
    private static int[][] corners;
    private static int placeCounter = 1;
    public volatile static int pointer;
    public volatile static boolean exitflag = false;
    public static ArrayList<Integer> cor = new ArrayList<>();


    public Player(Socket socket, Rules rules) throws IOException{
        this.client = socket;
        this.rules = rules;
        packageSender = new ObjectOutputStream(socket.getOutputStream());
        packageReader = new ObjectInputStream(socket.getInputStream());
        /*packageSender = new ObjectOutputStream(client.getOutputStream());
        packageReader = new ObjectInputStream(client.getInputStream());*/
    }

    @Override
    public void run() {
        try {
            while(client.isConnected()) {
                dataPackage = (DataPackage) packageReader.readObject();
                String command = dataPackage.getClientCommand();
                if(command.equals("Validate")) {
                    if(rules.checkMove(dataPackage)) {
                        if(rules.isWinning()) {
                            dataPackage.setServerResponse("You won");
                            dataPackage.setPlaceCounter(placeCounter);
                            placeCounter++;
                            if(placeCounter==players.size()) {
                                System.out.println("EXIT FLAG");
                                dataPackage.setExitFlag(true);
                            }
                            dataPackage.setSkipFlag(false);
                            dataPackage.setWinning(true);
                            dataPackage.setCurrentPlayer(nextPlayer(dataPackage.getCurrentPlayer()));
                            removePlayer(dataPackage.getCurrentPlayer());
                            packageSender.reset();
                            packageSender.writeObject(dataPackage);
                            packageSender.flush();
                            updatePlayers(dataPackage, "Update");
                        } else if(rules.isBlocking()) {
                            dataPackage.setServerResponse("You're Blocked");
                            packageSender.reset();
                            packageSender.writeObject(dataPackage);
                            packageSender.flush();
                            updatePlayers(dataPackage, "You're Blocked");

                        }
                        else if(rules.stillMove()) {
                            dataPackage.setServerResponse("Valid & move");
                            dataPackage.setSkipFlag(false);
                            packageSender.reset();
                            packageSender.writeObject(dataPackage);
                            packageSender.flush();
                            updatePlayers(dataPackage, "Update");
                        } else {
                            dataPackage.setServerResponse("Valid");
                            dataPackage.setSkipFlag(false);
                            dataPackage.setCurrentPlayer(nextPlayer(dataPackage.getCurrentPlayer()));
                            packageSender.reset();
                            packageSender.writeObject(dataPackage);
                            packageSender.flush();
                            updatePlayers(dataPackage, "Update");

                        }
                    } else {
                        dataPackage.setServerResponse("Invalid");
                        dataPackage.setSkipFlag(false);
                        packageSender.reset();
                        packageSender.writeObject(dataPackage);
                        packageSender.flush();
                    }
                } else if(command.equals("Skip")) {
                    dataPackage.setServerResponse("Skip");
                    dataPackage.setSkipFlag(true);
                    dataPackage.setCurrentPlayer(nextPlayer(dataPackage.getCurrentPlayer()));
                    packageSender.reset();
                    packageSender.writeObject(dataPackage);
                    packageSender.flush();
                    updatePlayers(dataPackage, "Update");
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
    public int nextPlayer(int player) {
        synchronized (Player.cor) {
            int index = Player.cor.indexOf(player);
            if (Player.cor.size() == 1) {
                return Player.cor.get(0);
            } else if (index + 1 == Player.cor.size()) {
                return Player.cor.get(0);
            } else {
                return Player.cor.get(index + 1);
            }
        }
       /* pointer=0;
        for(int i=0;i<players.size(); i++) {
            if(corners[i][0]==player) {
                pointer = i;
                while (true) {
                    if (pointer + 1 == players.size()) {
                        pointer=0;
                        if(corners[0][0]!=-1) {
                            break;
                        }
                    } else {
                        if(corners[pointer+1][0]==-1) {
                            pointer++;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        if(pointer==0) {
            return corners[0][0];
        } else {
            return corners[pointer + 1][0];
        }*/
    }
    public void removePlayer(int player) {
        synchronized (Player.cor) {
            Player.cor.remove((Integer) player);
            for (int i = 0; i < Player.cor.size(); i++) {
                System.out.println("TABELA" + Player.cor.get(i));
            }
        /*for(int i = 0; i<players.size();i++) {
            if(corners[i][0]==player) {
                corners[i][0]=-1;
            }
        }*/
        }
    }

    public void startGame(ArrayList<Player> players,int[][] corners) throws IOException {
        Player.corners=corners;
        this.players = players;
        for(int i = 0; i<players.size(); i++) {
            if(Player.cor.contains(corners[i][0])){
                break;
            } else {
                Player.cor.add(corners[i][0]);
            }
        }
        this.start();
    }

    private void updatePlayers(DataPackage dataPackage, String message) throws IOException {
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
