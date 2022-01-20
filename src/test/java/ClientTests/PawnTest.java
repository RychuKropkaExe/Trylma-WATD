package ClientTests;

import Client.Pawn;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.awt.*;

public class PawnTest {
    public Pawn pawn;
    @Before
    public void init() {
        pawn = new Pawn(new Point(100,100), new Color(100,100,100));
    }

    @Test
    public void setColorTest(){
        pawn.setColor(new Color(200,200,200));
        Assertions.assertEquals(pawn.getCircleColor(), new Color(200,200,200));

    }
    @Test
    public void containsCircleTest() {
        Assertions.assertTrue(pawn.containsCircle(new Point(110,110)));
    }
    @Test
    public void translateCircleTest() {
        pawn.translateCircle(new Point(10,10));
        Assertions.assertEquals(pawn.getCircleCenter(), new Point(110,110));
    }
    @Test
    public void setCircleLocationTest() {
        pawn.setCircleLocation(new Point(500,500));
        Assertions.assertEquals(pawn.getCircleCenter(), new Point(500,500));
    }
}
