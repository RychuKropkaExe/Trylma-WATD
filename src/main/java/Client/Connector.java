package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handles connection between Client and Server.
 */
public class Connector {


    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    /**
     * Constructor creates new I/O streams at given socket.
     *
     * @param socket  Socket to connect to
     * @throws IOException
     */
    public Connector(Socket socket) throws IOException {
        this.socket = socket;
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public Socket getSocket() {
        return socket;
    }
}
