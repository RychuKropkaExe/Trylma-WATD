package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Board extends JFrame implements MouseListener {
    private static ArrayList<Tile> circles = new ArrayList<>();
    private static ArrayList<MovableTile> tiles = new ArrayList<>();
    private boolean containsCircle=false;
    private Point point;
    private Point point2;
    private int index;
    private int k = 0;
    private int startingPoint=500;
    private int startingPoint2=500;
    private int startingPointY=460;
    private int startingPointY2=220;
    private int loop1=4;
    private int loop2=4;
    private Point Temp;
    private boolean Triangle1=true;
    private boolean Triangle2=false;
    private boolean Triangle3=false;
    private boolean Triangle4=true;
    private boolean Triangle5=false;
    private boolean Triangle6=true;
    public Board() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setVisible(true);
        MoveTile mover = new MoveTile();
        getContentPane().addMouseMotionListener(mover);
        getContentPane().addMouseListener(this);
        for(int i=13; i>=1;i--) {
            if(i<=4) {
                drawUsingFunction1(startingPoint,startingPoint2,i,true);
                startingPoint+=30;
                startingPoint2+=30;
            } else {
                drawUsingFunction1(startingPoint,startingPoint2, i, false);
                startingPoint+=30;
                startingPoint2+=30;
            }

        }
    }


    public void drawUsingFunction1(int startingPosition, int startingPoint2,int printCount, boolean paintField) {
        int x = startingPosition;
        int step=0;
        int step2=0;
        int loop=loop1;
        for(int i=0; i<printCount; i++) {
            point = new Point(x,startingPointY+step);
            System.out.println(point +" " + point2);
            point2 = new Point(x,startingPointY2+step2);
            if(!(i>=9)  || Triangle4) {
                addTile(point);
            }
            if(printCount<=4 && !Triangle6) {
                this.getContentPane().remove(circles.get(k - 1));
                circles.remove(k-1);
                k--;
                this.validate();
                this.repaint();
            }
            if(!Triangle2 && loop>=1){
                this.getContentPane().remove(circles.get(k - 1));
                circles.remove(k-1);
                k--;
                this.validate();
                this.repaint();
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
            x+=15;
            step-=30;
            step2+=30;
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
        for (int z=0; z<tiles.size();z++) {
            if ((tiles.get(z)).containsCircle(new Point(e.getX(), e.getY()))) {
                containsCircle = true;
                index=z;
                Tile temp =  circles.get(z);
                circles.remove(z);
                circles.add(temp);

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
                tiles.get(index).translateCircle(TranslateVector, e);
            }
        }
    }
    private void addTile(Point p) {
        circles.add(new Tile(p));
        k++;
        this.getContentPane().add(circles.get(k - 1));
        this.validate();
        this.repaint();
    }

}
