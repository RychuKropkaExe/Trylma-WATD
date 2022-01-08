package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Board extends JFrame implements MouseListener {


    private static final ArrayList<Tile> tiles = new ArrayList<>();
    private static final ArrayList<Pawn> pawns = new ArrayList<>();

    private Point point;
    private Point Temp;
    private Point point2;

    private final int startingPointY = 460;
    private final int startingPointY2 = 220;
    private int startingPoint = 500;
    private int loop1 = 4;
    private int index;
    private int k = 0;

    private final boolean Triangle1 = true;
    private final boolean Triangle2 = true;
    private final boolean Triangle3 = true;
    private final boolean Triangle4 = true;
    private final boolean Triangle5 = true;
    private final boolean Triangle6 = true;
    private boolean containsCircle = false;


    public Board() {
        initFrame();

        MoveTile mover = new MoveTile();
        getContentPane().addMouseMotionListener(mover);
        getContentPane().addMouseListener(this);

        for(int i=13; i>=1; i--) {
            if(i<=4) {
                drawUsingFunction1(startingPoint, i,true);
            } else {
                drawUsingFunction1(startingPoint, i,false);
            }
            startingPoint += 30;
        }
    }

    private void initFrame() {
        setTitle("Trylma-WATD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void drawUsingFunction1(int startingPosition, int printCount, boolean paintField) {

        int x = startingPosition;
        int step = 0;
        int step2 = 0;
        int loop = loop1;

        for(int i=0; i<printCount; i++) {

            point = new Point(x, (startingPointY + step));
            System.out.println(point + " " + point2);
            point2 = new Point(x, (startingPointY2 + step2));

            if(i<9 || Triangle4) {
                addTile(point);
            }

            if(printCount<=4 && !Triangle6) {
                getContentPane().remove(tiles.get(k - 1));
                tiles.remove(k-1);
                k--;

                validate();
                repaint();
            }

            if(!Triangle2 && loop>=1){
                getContentPane().remove(tiles.get(k - 1));
                tiles.remove(k-1);
                k--;

                validate();
                repaint();
            }

            if(i>=9 && Triangle1) {
                addTile(point2);
            }

            if(paintField && Triangle5) {
                addTile(point2);
            }

            if(loop>=1 && Triangle3) {
                addTile(point2);
            }

            x += 15;
            step -= 30;
            step2 += 30;
            loop--;
        }
        loop1--;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Temp = e.getPoint();

        for (int z=0; z<tiles.size(); z++) {
            if ((pawns.get(z)).containsCircle(new Point(e.getX(), e.getY()))) {
                containsCircle = true;
                index = z;
                Tile temp = tiles.get(z);

                tiles.remove(z);
                tiles.add(temp);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        containsCircle=false;
        index=-1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    class MoveTile extends MouseAdapter {
        public void mouseDragged(MouseEvent e) {
            //poruszanie figurą
            Point TranslateVector = new Point(e.getX() - Temp.x, e.getY() - Temp.y);
            Temp = e.getPoint();

            if (containsCircle) {
                System.out.println(containsCircle);
                pawns.get(index).translateCircle(TranslateVector, e);
            }
        }
    }

    private void addTile(Point p) {
        tiles.add(new Tile(p));
        getContentPane().add(tiles.get(k));
        k++;

        validate();
        repaint();
    }
}
