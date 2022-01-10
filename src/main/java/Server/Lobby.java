package Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A class that represents a lobby, in which the players are waiting for the game to start. It handles simple functionalities
 * like notifying players that someone has joined the lobby, and giving them unique names
 */
public class Lobby {


    /**
     * A list that stores players already connected to lobby
     */
    private static final ArrayList<Player> players = new ArrayList<>();
    /**
     * a number of players that are required to start the game
     */
    private final int playersQuantity;
    /**
     * counts how many players are there in lobby
     */
    private int counter = 0;
    private int[][] corners = new int[6][2];
    /**
     * Tells if lobby is full
     */
    private boolean isOpen = true;


    /**
     *
     * @param number a size of lobby that is created
     * @param king A creator of the lobby, he only differs in lobby not in actual game
     */
    public Lobby(int number, Player king, Boolean[] arms) throws IOException {
        System.out.println("DZIALA");
        playersQuantity = number;
        setPlayersArms();
        players.add(king);
        players.get(0).sendMessage("Lobby created successfully");
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
     * @param player a player to be added
     * @throws IOException
     */
    public void addPlayer(Player player) throws IOException {
        notifyPlayers("A player has joined the lobby");
        players.add(player);
        //players.get(counter).sendMessage("[Server] You have joined the lobby!");
        counter++;

        if(players.size()==playersQuantity) {
            isOpen = false;
            startGame();
        }
    }

    /**
     * starts the game if lobby is full
     */
    public void startGame() {
        /*for(Player player : players) {
            player.start();
        }*/
    }

    /**
     * send some message to all players in lobby
     * @param message
     */
    public void notifyPlayers(String message) throws IOException {
        for(int i = 0; i<players.size();i++) {
            PrintWriter tempWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(players.get(i).getSocket().getOutputStream())));
            tempWriter.println("A player has joined the lobby");
            tempWriter.flush();
        }
    }

    public void sendToSpecific(String message) {

    }

    /**
     * a method to check if lobby is full
     * @return true if lobby is full, false otherwise
     */
    public boolean isOpen() {
        return isOpen;
    }


}
