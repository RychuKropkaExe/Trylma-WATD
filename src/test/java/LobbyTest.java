import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LobbyTest {
    private ClientDummy clientDummy1;
    private ClientDummy clientDummy2;
    private ClientDummy clientDummy3;

    @Test
    public void testLobby() throws IOException, InterruptedException {
        clientDummy1 = new ClientDummy("127.0.0.1");
        clientDummy1.start();
        Assertions.assertEquals("[Server]Choose number of players",clientDummy1.getMessage());
        clientDummy1.sendMessage("4");
        Assertions.assertEquals("Lobby created successfully",clientDummy1.getMessage());
        clientDummy2 = new ClientDummy("127.0.0.2");
        Assertions.assertEquals("A player has joined the lobby", clientDummy1.getMessage());
        clientDummy2.start();
        Assertions.assertEquals("[Server] You have joined the lobby!", clientDummy2.getMessage());
        clientDummy3 = new ClientDummy("127.0.0.3");
        Assertions.assertEquals("A player has joined the lobby", clientDummy1.getMessage());
        Assertions.assertEquals("A player has joined the lobby", clientDummy2.getMessage());
        clientDummy3.start();
        Assertions.assertEquals("[Server] You have joined the lobby!", clientDummy3.getMessage());


    }
}
