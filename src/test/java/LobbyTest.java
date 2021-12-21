import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LobbyTest {
    private ClientDummy clientDummy1;
    private ClientDummy clientDummy2;

    @Test
    public void testLobby() throws IOException {
        clientDummy1 = new ClientDummy("127.0.0.2");
        Assertions.assertEquals("[Server]Choose number of players",clientDummy1.getMessage());
        clientDummy1.sendMessage("4");
        Assertions.assertEquals("Lobby created succesfully",clientDummy1.getMessage());
        clientDummy2 = new ClientDummy("localhost");
        Assertions.assertEquals("A player has joined the lobby", clientDummy1.getMessage());
    }
}
