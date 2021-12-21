import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientDummy {
    private static final int PORT =9090;
    private static Socket socket;
    private static String message;
    private static BufferedReader testInput;
    private static PrintWriter testOutput;
    public ClientDummy(String host) throws IOException {
        socket = new Socket(host,PORT);
        testOutput = new PrintWriter(socket.getOutputStream());
        testInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String getMessage() throws IOException {
        BufferedReader testInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if ((message = testInput.readLine()) != null) {
            return message;
        }
        return message;
    }
    public void sendMessage(String message) throws IOException{
        PrintWriter testOutput = new PrintWriter(socket.getOutputStream());
        testOutput.println(message);
        testOutput.flush();
    }
}