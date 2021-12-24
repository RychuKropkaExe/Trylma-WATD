import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientDummy extends Thread{
    private static final int PORT = 9090;
    private static Socket socket;
    private static volatile String message;
    private static DataInputStream testInput;
    private static DataOutputStream testOutput;

    public ClientDummy(String host) throws IOException {
        socket = new Socket("localhost",PORT);
        testOutput = new DataOutputStream(socket.getOutputStream());
        testInput = new DataInputStream(socket.getInputStream());
    }

    public synchronized String getMessage() throws IOException {
            return testInput.readUTF();
    }

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