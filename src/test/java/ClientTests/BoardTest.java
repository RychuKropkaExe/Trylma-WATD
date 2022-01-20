package ClientTests;

import Client.Board;
import Client.Pawn;
import Client.Tile;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.awt.*;
import java.util.ArrayList;

public class BoardTest {
    public Board testBoard;
    @Test
    public void TestReference() {
        Tile[] tiles = new Tile[6];
        tiles[0] = new Tile(new Point(0,0));
        tiles[1] = new Tile(new Point(10,10));
        tiles[2] = new Tile(new Point(20,20));
        tiles[3] = new Tile(new Point(30,30));
        tiles[4] = new Tile(new Point(40,40));
        tiles[5] = new Tile(new Point(50,50));
        for(int i=0; i<=5;i++) {
            for(int j=0; j<=4;j++) {
                if(i!=j) {
                    tiles[i].setNeighbour(j, tiles[j]);
                }
            }
        }
        tiles[2].getNeighbour(3).take();
        for(int i=0; i<=5;i++) {
            if(i!=3) {
                Assertions.assertTrue(tiles[i].getNeighbour(3).isTaken);
            }
        }

    }
    @Test
    public void testSetNeighbours() {
        Boolean[] starArm = new Boolean[6];
        starArm[0] = true;
        starArm[1] = true;
        starArm[2] = true;
        starArm[3] = true;
        starArm[4] = true;
        starArm[5] = true;
        testBoard = new Board(starArm, 0, 2);
        ArrayList<Tile> tiles = testBoard.getBoardTiles();
        Tile tile1 = tiles.get(0);
        Tile tile2 = tiles.get(2);
        boolean flag = false;
        for(int i = 0; i < 6 ; i++) {
            if(tile1.getNeighbour(i) != null) {
                if(tile1.getNeighbour(i).equals(tile2)) {
                    flag = true;
                }
            }
        }
        Assertions.assertTrue(flag);
     }
     @Test
     public void testDrawing() {
         Boolean[] starArm = new Boolean[6];
         starArm[0] = true;
         starArm[1] = true;
         starArm[2] = true;
         starArm[3] = true;
         starArm[4] = true;
         starArm[5] = true;
         testBoard = new Board(starArm, 0, 2);
         ArrayList<Pawn> pawns= testBoard.getPawns();
         Assertions.assertEquals(15,pawns.size());
         testBoard.drawUsingFunction(550, 13, true);
         pawns = testBoard.getPawns();
         Assertions.assertNotEquals(15,pawns.size());

     }
}
