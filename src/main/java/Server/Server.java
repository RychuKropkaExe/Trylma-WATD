package Server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 9090;
    private static CommandHandler commandHandler;
    private static ArrayList<Lobby> lobbies = new ArrayList<>();
    private static Player king;
    private static int lobbyCount=0;
    private static boolean isCreated = false;
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("The server is running!");
        while(true) {
            if(!isCreated) {
                Socket creator = listener.accept();
                System.out.println("[Server]A creator has logged!");
                king = new Player(creator);
                System.out.println(creator.getPort());
                king.sendMessage("[Server]Choose number of players");
                lobbies.add(new Lobby(Integer.parseInt(king.getServerMessage()),king));
                lobbyCount++;
                king = null;
                isCreated = true;
            }
            else {
                if(lobbies.get(lobbyCount-1).isOpen()) {
                    lobbies.get(lobbyCount-1).addPlayer(new Player(listener.accept()));
                }
                else {
                    isCreated = false;
                }
            }
        }

    }
}
