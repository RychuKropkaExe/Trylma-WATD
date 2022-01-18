package Server;

import Client.Pakiet;
import Server.Rules.SimpleRules;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import Client.*;

/**
 * A class that represents a lobby, in which the players are waiting for the game to start. It handles simple functionalities
 * like notifying players that someone has joined the lobby, and giving them unique names
 */
public class Lobby {


    /**
     * A list that stores players already connected to lobby
     */
    private static final ArrayList<Player> players = new ArrayList<>();

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * a number of players that are required to start the game
     */
    private final int playersQuantity;
    /**
     * counts how many players are there in lobby
     */
    private int counter = 0;
    private final static int[][] corners = new int[6][2];
    /**
     * Tells if lobby is full
     */
    private boolean isOpen = true;
    private final Boolean[] arms;
    int startingPlayer;


    public Lobby(Socket socket) throws IOException, ClassNotFoundException {
        players.add(new Player(socket, new SimpleRules()));
        playersQuantity = getPlayersQuantity();
        arms = getArms();
        setPlayersArms();
        //sendToSpecific("Lobby created successfully",0);
    }
    public void startGame() throws IOException, ClassNotFoundException {
        int startingPlayer = (int)(Math.random() * playersQuantity);
        notifyPlayers("The game is starting!");
        for(int i = 0; i<players.size(); i++) {
            for(int j=0; j<6; j++) {
                players.get(i).sendMessage(String.valueOf(arms[j]));
            }
            players.get(i).sendMessage(corners[i][0]);
            players.get(i).sendMessage(playersQuantity);
            players.get(i).sendMessage(corners[i][1]);
            players.get(i).sendMessage(corners[startingPlayer][0]);

        }
        for(int i=0; i<players.size(); i++) {
            players.get(i).startGame(players,corners);
        }
    }
    private int getPlayersQuantity() throws IOException, ClassNotFoundException {
        players.get(0).sendMessage("[Server]Choose number of players");
        Pakiet pakiet = players.get(0).getMessage();
        return pakiet.num;
    }
    private Boolean[] getArms() throws IOException, ClassNotFoundException {
        Boolean[] temp = new Boolean[6];

        for(int i = 0; i<6; i++) {
            Pakiet pakiet = (Pakiet) players.get(0).getMessage();
            temp[i] = Boolean.parseBoolean(pakiet.command);
        }

        return temp;
    }

    private void setPlayersArms() {
        if(playersQuantity == 2) {
            corners[0][0] = 0;
            corners[0][1] = 3;
            corners[1][0] = 3;
            corners[1][1] = 0;
        } else if(playersQuantity == 3) {
            corners[0][0] = 1;
            corners[0][1] = 4;
            corners[1][0] = 3;
            corners[1][1] = 0;
            corners[2][0] = 5;
            corners[2][1] = 2;
        } else if(playersQuantity == 4) {
            corners[0][0] = 1;
            corners[0][1] = 4;
            corners[1][0] = 2;
            corners[1][1] = 5;
            corners[2][0] = 4;
            corners[2][1] = 1;
            corners[3][0] = 5;
            corners[3][1] = 2;
        } else if(playersQuantity == 6) {
            for(int i = 0; i<6; i++) {
                corners[i][0] = i;
                corners[i][1] = i+3;
            }
        }
    }

    /**
     * Add player to lobby, and notify other players about it
     * @param socket a socket used by client
     * @throws IOException
     */
    public void addPlayer(Socket socket) throws IOException, ClassNotFoundException {
        notifyPlayers("A player has joined the lobby");
        players.add(new Player(socket, new SimpleRules()));
        sendToSpecific("[Server] You have joined the lobby!", players.size()-1);
        //players.get(counter).sendMessage("[Server] You have joined the lobby!");
        counter++;

        if(players.size()==playersQuantity) { ;
            isOpen = false;
            startGame();
        }
    }

    /**
     * starts the game if lobby is full
     */

    /**
     * send some message to all players in lobby
     * @param message
     */
    public void notifyPlayers(String message) throws IOException {
        for(int i = 0; i<players.size();i++) {
            Pakiet pakiet = new Pakiet(message);
            players.get(i).getOutput().writeObject(pakiet);
            players.get(i).getOutput().flush();
        }
    }

    public void sendToSpecific(String message, int i) throws IOException {
        Pakiet pakiet = new Pakiet(message);
        players.get(i).getOutput().writeObject(pakiet);
        players.get(i).getOutput().flush();;
    }

    /**
     * a method to check if lobby is full
     * @return true if lobby is full, false otherwise
     */
    public boolean isOpen() {
        return isOpen;
    }


}
