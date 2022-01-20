package ServerTests;

import Client.Board;
import Client.DataPackage;
import Client.Tile;
import Server.Rules.Rules;
import Server.Rules.SimpleRules;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.awt.*;

public class SimpleRulesTest {
    Board testBoard;
    Rules rules;
    DataPackage data;

    @Before
    public void init() {
        Boolean[] starArm = new Boolean[6];
        starArm[0] = true;
        starArm[1] = true;
        starArm[2] = true;
        starArm[3] = true;
        starArm[4] = true;
        starArm[5] = true;
        testBoard = new Board(starArm, 0, 2);
        data = new DataPackage(testBoard.getBoardTiles(), testBoard.getPawns(), testBoard.getMovablePawns(), testBoard.getWinPoints());
        rules = new SimpleRules();
    }

    @Test
    public void testCheckMove() {
        data.setStartingTileIndex(0);
        data.setDropTileIndex(2);
        rules.setBoardVariables(data);
        Assertions.assertTrue(rules.validateMove());
        Assertions.assertFalse(rules.stillMove());
        Assertions.assertFalse(rules.isBlocking());
        Assertions.assertFalse(rules.isWinning());

    }
    @Test
    public void TestDoubleJump() {
        data.setStartingTileIndex(0);
        data.getClientTiles().get(2).take();
        data.setDropTileIndex(4);
        data.getClientTiles().get(6).take();
        rules.setBoardVariables(data);
        Assertions.assertTrue(rules.validateMove());
        Assertions.assertTrue(rules.stillMove());
        Assertions.assertFalse(rules.isWinning());
        Assertions.assertFalse(rules.isBlocking());
    }
}
