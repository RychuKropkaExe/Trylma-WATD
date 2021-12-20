package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 2137;
    private static CommandHandler commandHandler;
    private static ArrayList<Player> players = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("The server is running!");
        while(true) {
            if(players.size() == 0) {
                Socket creator = listener.accept();

            }
        }

    }
}
