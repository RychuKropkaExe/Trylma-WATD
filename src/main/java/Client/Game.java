package Client;

import java.io.IOException;
import java.net.Socket;

/**
 * Main class used to initialize the game.
 * It needs running Server to start.
 */
public class Game {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",9090);

        new Starter(new Connector(socket));
    }
}
