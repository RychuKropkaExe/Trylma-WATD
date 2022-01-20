package ServerTests;

import Server.Lobby;
import Server.Player;
import Server.Rules.SimpleRules;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Stream;

public class PlayerTest {
    public static final ArrayList<Integer> cor = new ArrayList<>();
    FileInputStream fi;
    FileOutputStream fo;
    Socket socket;
    Player player;
    @Before
    public void init() throws IOException {
        socket = Mockito.mock(Socket.class);
        fi = new FileInputStream("DummyFile.txt");
        fo = new FileOutputStream("DummyFile.txt");
        Socket socket = Mockito.mock(Socket.class);
        Mockito.when(socket.getInputStream()).thenReturn(fi);
        Mockito.when(socket.getOutputStream()).thenReturn(fo);
        player = new Player(socket, new SimpleRules());

    }

    @Test
    public void playerTest() throws IOException, ClassNotFoundException {
        cor.add(0);
        cor.add(1);
        cor.add(2);
        cor.add(3);
        cor.add(4);
        cor.add(5);
        Player.cor = cor;
        Assertions.assertEquals(1,player.nextPlayer(0));
        player.removePlayer(2);
        Assertions.assertEquals(3, player.nextPlayer(1));

    }
    @Test
    public void messageTest() throws IOException, ClassNotFoundException {
        player.sendMessage(2);
        player.sendMessage("This test is pointless");
    }
    @After
    public void close() throws IOException {
        fi.close();
        fo.close();
    }
}
