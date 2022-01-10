package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Starts new Thread for each new Client.
 */
public class ClientThread extends Thread {


    private final Socket socket;
    private static DataInputStream testInput;
    private static DataOutputStream testOutput;


    /**
     * Creates Output and Input Streams for given Socket.
     *
     * @param socket  Server's socket
     * @throws IOException
     */
    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        testOutput = new DataOutputStream(socket.getOutputStream());
        testInput = new DataInputStream(socket.getInputStream());
    }


    /**
     * Gets message from the server.
     *
     * @return The message
     * @throws IOException
     */
    public synchronized String getMessage() throws IOException {
        return testInput.readUTF();
    }

    /**
     * Sends message to the server.
     *
     * @param message The message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        testOutput.writeUTF(message);
    }

    @Override
    public void run() {

    }

    public Socket getSocket() {
        return socket;
    }
}
