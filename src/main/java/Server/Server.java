package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {


    private static final ArrayList<Lobby> lobbies = new ArrayList<>();
    private static final ArrayList<Socket> sockets = new ArrayList<>();

    private static CommandHandler commandHandler;
    private static BufferedReader input;
    private static PrintWriter output;
    private static Player king;

    private static final int PORT = 9090;
    private static int lobbyCount = 0;

    private static boolean isCreated = false;


    private static Boolean[] getArms() throws IOException {
        Boolean[] temp = new Boolean[6];

        for(int i = 0; i<6; i++) {
            temp[i] = Boolean.parseBoolean(input.readLine());
        }

        return temp;
    }

    public static void main(String[] args) throws IOException {

        ServerSocket listener = new ServerSocket(PORT);

        System.out.println("The server is running!");

        while(true) {
            if(!isCreated) {
                sockets.add(listener.accept());
                System.out.println("[Server]A creator has logged!");

                king = new Player(sockets.get(0));
                input = new BufferedReader(new InputStreamReader(sockets.get(0).getInputStream()));
                output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockets.get(0).getOutputStream())));

                output.println("[Server]Choose number of players");
                output.flush();

                int numP = Integer.parseInt(input.readLine());
                lobbies.add(new Lobby(numP,king,getArms()));
                king.start();

                lobbyCount++;
                isCreated = true;
            }
            else {
                if(lobbies.get(lobbyCount-1).isOpen()) {
                    Socket p = listener.accept();
                    sockets.add(p);
                    System.out.println(p.getPort());

                    Player player = new Player(p);
                    player.start();
                    lobbies.get(lobbyCount-1).addPlayer(player);
                } else {
                    isCreated = false;
                }
            }
        }

    }
}
