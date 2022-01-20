package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connector {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    public Connector(Socket socket) throws IOException {
        this.socket=socket;
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
