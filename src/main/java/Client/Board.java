package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Creates JFrame with the game board for each player.
 */
public class Board extends JFrame implements MouseListener {


    /** ClientThread object used for creating game for each Player */
    public ClientThread game;

    /** ArrayLists containing tiles and pawns that create the board. */
    private final ArrayList<Tile> tiles = new ArrayList<>();
    private final ArrayList<Pawn> pawns = new ArrayList<>();
    private final ArrayList<Pawn> movablePawns = new ArrayList<>();
    private final ArrayList<Point> winPoints = new ArrayList<>();

    private final MoveTile mover = new MoveTile();

    /** Point variables used for generating board. */
    private Point point;
    private Point point2;
    private Point savedPosition;
    private Point temp;

    private final int startingArm;
    private final int players;
    private int startingPoint = 550;
    private int k = 0;
    private int loop1 = 4;
    private int pawnsCounter= 0;
    private int movablePawnsCounter = 0;
    private int liftedPawnIndex;
    private int startingTileIndex;
    private int dropTileIndex;
    private int winArm;

    private Socket socket;

    private final Boolean[] starArm;
    private boolean containsCircle = false;
    private boolean isPlaying = true;

    private ObjectInputStream packageReader;
    private ObjectOutputStream packageSender;

    /**
     * Board constructor creates the game board and starts new Thread for every player.
     *
     * @param arms  Array with information which arms shall be drawn
     * @param playerID  ID of staring player
     * @param players  Number of players
     * @throws IOException
     */
    public Board(Boolean[] arms, int playerID,int players, int winArm, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        starArm = arms;
        this.winArm = winArm;
        startingArm = playerID;
        this.players = players;
        packageReader = input;
        packageSender = output;

        int myInt = (players==2) ? 1 : 0;

        initFrame();

        for(int i=(13+myInt); i>=1; i--) {
            if(i<=4) {
                drawUsingFunction(startingPoint, i,true);
            } else {
                drawUsingFunction(startingPoint, i,false);
            }
            startingPoint += 30;
        }

        setNeighbours();
        startGame();
    }

    public Board(Boolean[] arms, int playerID, int players) {
        starArm = arms;
        startingArm = playerID;
        this.players = players;

        int myInt = (players==2) ? 1 : 0;

        initFrame();


        for(int i=13+myInt; i>=1; i--) {
            if(i<=4) {
                drawUsingFunction(startingPoint, i,true);
            } else {
                drawUsingFunction(startingPoint, i,false);
            }
            startingPoint += 30;
        }

        setNeighbours();
        validate();
        repaint();
    }

    /**
     * Creates JFrame and Mouse Listeners.
     */
    private void initFrame() {
        getContentPane().addMouseMotionListener(mover);
        getContentPane().addMouseListener(this);
        getContentPane().setBackground(new Color(117, 148, 229));
        setTitle("Trylma-WATD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void startGame() throws IOException {
        new Thread(()->{
            try {
                while (isPlaying) {
                    DataPackage serverData = (DataPackage)packageReader.readObject();
                    String response = serverData.getServerResponse();
                    if(response.equals("Valid")) {
                        int MPindex = serverData.getValidatedMPawnIndex();
                        int Tindex = serverData.getDropTileIndex();
                        int STindex = serverData.getStartingTileIndex();
                        Point newLocation = tiles.get(Tindex).getCircleCenter();
                        tiles.get(STindex).leave();
                        tiles.get(Tindex).take();
                        movablePawns.get(MPindex).setCircleLocation(newLocation);
                    }
                    if(response.equals("Update")) {
                        Point oldPawnL = serverData.getOldPawnLocation();
                        Point newPawnL = serverData.getNewPawnLocation();
                        int Tindex = serverData.getDropTileIndex();
                        int STindex = serverData.getStartingTileIndex();
                        tiles.get(STindex).leave();
                        tiles.get(Tindex).take();
                        for(Pawn pawn : pawns) {
                            if(pawn.getCircleCenter().equals(oldPawnL)) {
                                pawn.setCircleLocation(newPawnL);
                            }
                        }

                    }
                }
            } catch(IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void sendRequest(String command) throws IOException {
        DataPackage clientData = new DataPackage(tiles, pawns, movablePawns,winPoints);
        if(command.equals("skip")) {
            clientData.setClientCommand(command);
        } else {
            clientData.setClientCommand(command);
            clientData.setStartingTileIndex(startingTileIndex);
            clientData.setDropTileIndex(dropTileIndex);
            clientData.setLiftedPawnIndex(liftedPawnIndex);
        }
        packageSender.reset();
        packageSender.writeObject(clientData);
        packageSender.flush();
    }
    /**
     * Generates the game board and sets each Player homes.
     *
     * TODO:
     * @param startingPosition
     * @param printCount
     * @param paintField
     */
    public void drawUsingFunction(int startingPosition, int printCount, boolean paintField) {

        int x = startingPosition;
        int step = 0;
        int step2 = 0;
        int loop = loop1;

        for(int i=0; i<printCount; i++) {

            int startingPointY = 460;
            int startingPointY2 = 220;

            point = new Point(x, (startingPointY + step));
            System.out.println(point + " " + point2);
            point2 = new Point(x, (startingPointY2 + step2));

            if(i<9 || starArm[3]) {
                addTile(point);
                if(i>=9 && (players==2 || players == 3 || players ==6)){
                    addPawn(point, new Color(89,0,165), 3);
                    tiles.get(k-1).take();
                }
            }

            if(printCount<=4) {
                if(!starArm[5]) {
                    getContentPane().remove(tiles.get(k - 1));
                    tiles.remove(k - 1);
                    k--;
                    validate();
                    repaint();
                } else if(players==3 || players==4 || players==6) {
                    addPawn(point, Color.BLUE, 5);
                    tiles.get(k-1).take();
                }
            }

            if(!starArm[1] && loop>=1) {
                getContentPane().remove(tiles.get(k - 1));
                tiles.remove(k-1);
                k--;
                validate();
                repaint();
            } else if(starArm[1] && loop>=1 && (players==6 || players==4 || players==3)) {
                addPawn(point, Color.GREEN,1);
                tiles.get(k-1).take();
            }

            if(i>=9 && starArm[0]) {
                addTile(point2);
                if(players==2 || players==6) {
                    addPawn(point2, new Color(199,24,24),0);
                    tiles.get(k-1).take();
                }
            }

            if(paintField && starArm[4]) {
                addTile(point2);
                if(players==4 || players==6) {
                    addPawn(point2, new Color(241,194,50),4);
                    tiles.get(k-1).take();
                }
            }

            if(loop>=1 && starArm[2]) {
                addTile(point2);
                if(players==4 || players==6){
                    addPawn(point2, new Color(247,19,132),2);
                    tiles.get(k-1).take();
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
        temp = e.getPoint();

        for (int z=0; z<movablePawns.size(); z++) {
            if ((movablePawns.get(z)).containsCircle(new Point(e.getX(), e.getY()))) {
                System.out.println("TEST");

                containsCircle = true;
                savedPosition = new Point(movablePawns.get(z).getCircleCenter());
                liftedPawnIndex = z;
                for(int i = 0; i< tiles.size(); i++) {
                    if(tiles.get(i).getCircleCenter()==savedPosition) {
                        startingTileIndex=i;
                        break;
                    }
                }
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

            for (int z = 0; z< tiles.size(); z++) {
                if ((tiles.get(z)).containsCircle(dropPoint)) {
                    /*savedPosition = new Point(movablePawns.get(z).getX(), movablePawns.get(z).getY());
                    index = z;*/
                    //Tile temp = circles.get(z);
                    dropTileIndex = z;
                    try {
                        sendRequest("Validate");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
/*                    if(tiles.get(z).isTaken){
                        break;
                    } else {
                        getContentPane().remove(movablePawns.get(liftedPawnIndex));
                        movablePawns.get(liftedPawnIndex).setCircleLocation(tiles.get(z).getCircleCenter());
                        tiles.get(z).take();

                        for(Tile tile: tiles) {
                            if(tile.getCircleCenter().equals(savedPosition)) {
                               tile.leave();
                               break;
                            }
                        }
                        getContentPane().add(movablePawns.get(liftedPawnIndex), 0);
                        validate();
                        repaint();*/
                       /* containsCircle = false;
                        liftedPawnIndex = -1;

                        return;*/
                    }
                }
                /*circles.remove(z);
                circles.add(temp);*/
            }
/*                System.out.println("GUCCI");

                getContentPane().remove(movablePawns.get(liftedPawnIndex));
                movablePawns.get(liftedPawnIndex).setCircleLocation(savedPosition);
                getContentPane().add(movablePawns.get(liftedPawnIndex),0);
                validate();
                repaint();
        }*/
        containsCircle = false;
        liftedPawnIndex = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    class MoveTile extends MouseAdapter {

        public void mouseDragged(MouseEvent e) {
            Point translateVector = new Point(e.getX() - temp.x, e.getY() - temp.y);
            temp = e.getPoint();

            if (containsCircle) {
                movablePawns.get(liftedPawnIndex).translateCircle(translateVector, e);
            }
        }
    }

    /**
     * Adds new Tile into Arraylist with given Point, then draws it on the JFrame.
     *
     * @param p  Point containing coordinates of the Tile
     */
    private void addTile(Point p) {
        tiles.add(new Tile(p));
        getContentPane().add(tiles.get(k));
        k++;
        validate();
        repaint();
    }

    /**
     * Checks whether TODO: ???
     * Then adds new Pawns into ArrayList with given Point and Color and draws them on the JFrame.
     *
     * @param p  Point containing coordinates of the Pawn
     * @param c  Color of the Pawn
     * @param arm  TODO: ???
     */
    private void addPawn(Point p, Color c, int arm) {
        if(arm==startingArm){
            movablePawns.add(new Pawn(p,c));
            getContentPane().add(movablePawns.get(movablePawnsCounter),0);
            movablePawnsCounter++;
        } else if(arm==winArm) {
            winPoints.add(p);
            pawns.add(new Pawn(p, c));
            getContentPane().add(pawns.get(pawnsCounter), 0);
            pawnsCounter++;
        } else {
            pawns.add(new Pawn(p, c));
            getContentPane().add(pawns.get(pawnsCounter), 0);
            pawnsCounter++;
        }
        validate();
        repaint();
    }

    /**
     * Sets neighbour for every possible Tile on the board.
     */
    private void setNeighbours() {
        for(Tile tile: tiles) {
            int x1 = tile.getX();
            int y1 = tile.getY();

            for(Tile possibleNeighbour: tiles) {
                int x2 = possibleNeighbour.getX();
                int y2 = possibleNeighbour.getY();
                int dx = x1-x2;
                int dy = y1-y2;

                if(dx==30 && dy==0) {
                    tile.setNeighbour(0, possibleNeighbour);
                }
                if(dx==15 && dy==30) {
                    tile.setNeighbour(1, possibleNeighbour);
                }
                if(dx==-15 && dy==30) {
                    tile.setNeighbour(2, possibleNeighbour);
                }
                if(dx == -30 && dy==0) {
                    tile.setNeighbour(3, possibleNeighbour);
                }
                if(dx==-15 && dy==-30) {
                    tile.setNeighbour(4, possibleNeighbour);
                }
                if(dx==15 && dy==-30) {
                    tile.setNeighbour(5, possibleNeighbour);
                }
            }
            return;
        }
    }
}
