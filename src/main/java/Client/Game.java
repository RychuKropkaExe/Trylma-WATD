package Client;

import java.io.IOException;
import java.net.Socket;

public class Game {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",9090);
        new Starter(new Connector(socket));
    }
}
