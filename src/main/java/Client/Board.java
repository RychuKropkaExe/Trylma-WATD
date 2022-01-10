package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Board extends JFrame implements MouseListener {


    private static final ArrayList<Tile> circles = new ArrayList<>();
    private static final ArrayList<Pawn> pawns = new ArrayList<>();
    private static final ArrayList<Pawn> movablePawns = new ArrayList<>();

    private Point point;
    private Point Temp;
    private Point point2;
    private Point savedPosition;

    private final int startingPointY = 460;
    private final int startingPointY2 = 220;
    private int startingPoint = 500;
    private int loop1 = 4;
    private int index;
    private int k = 0;
    private int players;
    private int pawnsCounter= 0;
    private int startingArm;
    private int movablePawnsCounter = 0;

    private boolean containsCircle = false;
    private Boolean[] starArm;

    MoveTile mover = new MoveTile();

    ClientThread game;


    public Board(Boolean[] arms, int playerID, int players, Socket socket) throws IOException {
        this.players=players;
        starArm = arms;
        startingArm = playerID;
        initFrame();
        int myInt = (players==2) ? 1:0;

        for(int i=13+myInt; i>=1; i--) {
            if(i<=4) {
                drawUsingFunction1(startingPoint, i,true);
            } else {
                drawUsingFunction1(startingPoint, i,false);
            }
            startingPoint += 30;
        }
        setNeighbours();
        game = new ClientThread(socket);

    }

    private void initFrame() {
        getContentPane().addMouseMotionListener(mover);
        getContentPane().addMouseListener(this);
        getContentPane().setBackground(Color.YELLOW);
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

            if(i<9 || starArm[3]) {
                addTile(point);
                if(i>=9 && (players==2 || players == 3 || players ==6)){
                    addPawn(point, new Color(89,0,165), 3);
                    circles.get(k-1).take();
                }
            }

            if(printCount<=4) {
                if(!starArm[5]) {
                    getContentPane().remove(circles.get(k - 1));
                    circles.remove(k - 1);
                    k--;
                    validate();
                    repaint();
                } else if(players==3 || players==4 || players==6) {
                    addPawn(point, Color.BLUE, 5);
                    circles.get(k-1).take();
                }
            }

            if(!starArm[1] && loop>=1) {
                getContentPane().remove(circles.get(k - 1));
                circles.remove(k-1);
                k--;
                validate();
                repaint();
            } else if(starArm[1] && loop>=1 && (players==6 || players==4 || players==3)) {
                addPawn(point, Color.GREEN,1);
                circles.get(k-1).take();
            }

            if(i>=9 && starArm[0]) {
                addTile(point2);
                if(players==2 || players==6) {
                    addPawn(point2, new Color(199,24,24),0);
                    circles.get(k-1).take();
                }
            }

            if(paintField && starArm[4]) {
                addTile(point2);
                if(players==4 || players==6) {
                    addPawn(point2, new Color(241,194,50),4);
                    circles.get(k-1).take();
                }
            }

            if(loop>=1 && starArm[2]) {
                addTile(point2);
                if(players==4 || players ==6){
                    addPawn(point2, new Color(247,19,132),2);
                    circles.get(k-1).take();
                }
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

        for (int z=0; z<movablePawns.size(); z++) {
            if ((movablePawns.get(z)).containsCircle(new Point(e.getX(), e.getY()))) {
                System.out.println("TEST");
                containsCircle = true;
                savedPosition = new Point(movablePawns.get(z).getCircleCenter());
                index = z;
                //Tile temp = circles.get(z);
                getContentPane().remove(movablePawns.get(z));
                getContentPane().add(movablePawns.get(z),0);
                return;
                /*circles.remove(z);
                circles.add(temp);*/
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(containsCircle) {
            Point dropPoint = e.getPoint();
            for (int z=0; z<circles.size(); z++) {
                if ((circles.get(z)).containsCircle(dropPoint)) {
                    /*savedPosition = new Point(movablePawns.get(z).getX(), movablePawns.get(z).getY());
                    index = z;*/
                    //Tile temp = circles.get(z);
                    if(circles.get(z).isTaken){
                        break;
                    } else {
                        getContentPane().remove(movablePawns.get(index));
                        movablePawns.get(index).setCircleLocation(circles.get(z).getCircleCenter());
                        circles.get(z).take();
                        for(Tile tile: circles) {
                            if(tile.getCircleCenter().equals(savedPosition)) {
                               tile.leave();
                               break;
                            }
                        }
                        getContentPane().add(movablePawns.get(index), 0);
                        validate();
                        repaint();
                        containsCircle = false;
                        index = -1;
                        return;
                    }
                }
                /*circles.remove(z);
                circles.add(temp);*/
            }
                System.out.println("GUCCI");
                getContentPane().remove(movablePawns.get(index));
                movablePawns.get(index).setCircleLocation(savedPosition);
                getContentPane().add(movablePawns.get(index),0);
                validate();
                repaint();
        }
        containsCircle = false;
        index = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    class MoveTile extends MouseAdapter {
        public void mouseDragged(MouseEvent e) {
            Point TranslateVector = new Point(e.getX() - Temp.x, e.getY() - Temp.y);
            Temp = e.getPoint();

            if (containsCircle) {
                movablePawns.get(index).translateCircle(TranslateVector, e);
            }
        }
    }

    private void addTile(Point p) {
        circles.add(new Tile(p));
        getContentPane().add(circles.get(k));
        k++;
        validate();
        repaint();
    }

    private void addPawn(Point p, Color c, int arm) {
        if(arm==startingArm){
            movablePawns.add(new Pawn(p,c));
            getContentPane().add(movablePawns.get(movablePawnsCounter),0);
            movablePawnsCounter++;
        } else {
            pawns.add(new Pawn(p, c));
            getContentPane().add(pawns.get(pawnsCounter), 0);
            pawnsCounter++;
        }
        validate();
        repaint();
    }

    private void setNeighbours() {
        for(Tile tile: circles) {
            int x1 = tile.getX();
            int y1 = tile.getY();

            for(Tile possibleNeighbour: circles) {
                int x2 = possibleNeighbour.getX();
                int y2 = possibleNeighbour.getY();
                int dx = x1-x2;
                int dy = y1-y2;

                if(dx==30 && dy==0) {
                    tile.setNeighbour(0, possibleNeighbour);
                }
                if(dx==15 && dy==30) {
                    tile.setNeighbour(1,possibleNeighbour);
                }
                if(dx==-15 && dy==30) {
                    tile.setNeighbour(2,possibleNeighbour);
                }
                if(dx == -30 && dy==0) {
                    tile.setNeighbour(3,possibleNeighbour);
                }
                if(dx==-15 && dy==-30) {
                    tile.setNeighbour(4,possibleNeighbour);
                }
                if(dx==15 && dy==-30) {
                    tile.setNeighbour(5,possibleNeighbour);
                }
            }
            return;
        }
    }
}
