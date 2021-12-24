package Server;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A class that represents a lobby, in which the players are waiting for the game to start. It handles simple functionalities
 * like notifying players that someone has joined the lobby, and giving them unique names
 */
public class Lobby {
    /**
     * a number of players that are required to start the game
     */
    private int playersQuantity;
    /**
     * counts how many players are there in lobby
     */
    private int counter = 0;
    /**
     * A list that stores players already connected to lobby
     */
    private static ArrayList<Player> players = new ArrayList<>();
    /**
     * Tells if lobby is full
     */
    private boolean isOpen = true;

    /**
     *
     * @param number a size of lobby that is created
     * @param king A creator of the lobby, he only differs in lobby not in actual game
     * @throws IOException
     */
    public Lobby(int number, Player king) throws IOException {
        playersQuantity = number;
        players.add(king);
        players.get(counter).sendMessage("Lobby created successfully");
        counter++;
    }

    /**
     * Add player to lobby, and notify other players about it
     * @param player a player to be added
     * @throws IOException
     */
    public void addPlayer(Player player) throws IOException {
        notifyPlayers("A player has joined the lobby");
        players.add(player);
        players.get(counter).sendMessage("[Server] You have joined the lobby!");
        counter++;
        if(players.size()==playersQuantity) {
            isOpen=false;
            startGame();
        }
    }

    /**
     * starts the game if lobby is full
     */
    public void startGame() {
        for(Player player : players) {
            player.start();
        }
    }

    /**
     * send some message to all players in lobby
     * @param message
     * @throws IOException
     */
    public void notifyPlayers(String message) throws IOException {
        for(Player player : players) {
            player.sendMessage(message);
        }
    }

    /**
     * a method to check if lobby is full
     * @return true if lobby is full, false otherwise
     */
    public boolean isOpen() {
        return isOpen;
    }


}
