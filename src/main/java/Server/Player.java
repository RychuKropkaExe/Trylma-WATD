package Server;

import java.io.*;
import java.net.Socket;

public class Player implements Runnable{
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;
    public Player(Socket socket) throws IOException{
        client=socket;
        output = new PrintWriter(client.getOutputStream());
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }
    @Override
    public void run(){
        try {
            while(true) {
                String command = input.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            output.close();
        }

    }
}
