package Server;

import Client.Pakiet;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

    private static final ArrayList<Lobby> lobbies = new ArrayList<>();
    private static final ArrayList<Socket> sockets = new ArrayList<>();

    private static final int PORT = 9090;
    private static int lobbyCount = 0;

    private static boolean isCreated = false;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ServerSocket listener = new ServerSocket(PORT);

        System.out.println("The server is running!");

        while(true) {
            if(!isCreated) {
                //Przenieść całość tego do lobby
                sockets.add(listener.accept());
                System.out.println("[Server]A creator has logged!");

                lobbies.add(new Lobby(sockets.get(0)));
                //king.start();

                lobbyCount++;
                isCreated = true;
            }
            else {
                if(lobbies.get(lobbyCount-1).isOpen()) {
                    Socket p = listener.accept();
                    sockets.add(p);
                    System.out.println(p.getPort());
//                  player.start();
                    lobbies.get(lobbyCount-1).addPlayer(p);
                } else {
                    isCreated = false;
                }
            }
        }

    }
}
