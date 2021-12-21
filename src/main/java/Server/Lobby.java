package Server;

import java.util.ArrayList;

public class Lobby {
    private int playersQuantity;
    private static ArrayList<Player> players = new ArrayList<>();
    private boolean isOpen = true;
    public Lobby(int number, Player king) {
        playersQuantity = number;
        players.add(king);
        players.get(0).sendMessage("Lobby created succesfully");
    }
    public void addPlayer(Player player) {
        notifyPlayers("A player has joined the lobby");
        players.add(player);
        if(players.size()==playersQuantity) {
            isOpen=false;
            startGame();
        }
    }
    public void startGame() {
        for(Player player : players) {
            player.run();
        }
    }
    public void notifyPlayers(String message) {
        for(Player player : players) {
            player.sendMessage(message);
        }
    }
    public boolean isOpen() {
        return isOpen;
    }


}
