package ClientTests;

import Client.Tile;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.awt.*;

public class TileTest {
    Tile testTile;
    @Before
    public void init() {
        testTile = new Tile(new Point(100,100));
    }

    @Test
    public void testTake(){
        testTile.take();
        Assertions.assertTrue(testTile.isTaken());

    }
    @Test
    public void testLeave(){
        testTile.isTaken=true;
        testTile.leave();
        Assertions.assertFalse(testTile.isTaken());

    }
    @Test
    public void testContainsCircle() {
        Assertions.assertTrue(testTile.containsCircle(new Point(110,110)));

    }
}
