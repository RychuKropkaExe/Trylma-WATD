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


    /** ArrayLists containing tiles and pawns that create the board. */
    private final ArrayList<Tile> tiles = new ArrayList<>();
    private final ArrayList<Pawn> pawns = new ArrayList<>();
    private final ArrayList<Pawn> movablePawns = new ArrayList<>();
    private final ArrayList<Point> winPoints = new ArrayList<>();

    private final MoveTile mover = new MoveTile();

    private Point temp;

    private final int startingArm;
    private final int players;
    private int startingPoint = 550;
    private int k = 0;
    private int loop1 = 4;
    private volatile int pawnsCounter= 0;
    private volatile int movablePawnsCounter = 0;
    private volatile int liftedPawnIndex;
    private volatile int startingTileIndex;
    private volatile int dropTileIndex;
    private volatile int winArm;

    private final JButton skipButton = new JButton("Skip");

    private final Boolean[] starArm;
    private final boolean isPlaying = true;
    private boolean containsCircle = false;

    private ObjectInputStream packageReader;
    private ObjectOutputStream packageSender;

    private JPanel panel;
    private JTextArea textArea;


    /**
     * Board constructor creates the game board and starts new Thread for every player.
     *
     * @param arms  Array with information which arms shall be drawn
     * @param playerID  ID of staring player
     * @param players  Number of players
     * @throws IOException
     */
    public Board(Boolean[] arms, int playerID, int players, int winArm, int startingPlayer, Connector connector) throws IOException {
        starArm = arms;
        startingArm = playerID;
        this.players = players;
        this.winArm = winArm;
        packageReader = connector.getInput();
        packageSender = connector.getOutput();

        int myInt = (players==2) ? 1 : 0;

        initFrame();

        if(playerID!=startingPlayer) {
            panel.removeMouseListener(this);
            getContentPane().removeMouseListener(this);
            getContentPane().removeMouseMotionListener(mover);
            textArea.setText("OPPONENT'S TURN");
        } else {
            textArea.setText("YOUR TURN");
        }

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
        panel = new JPanel();
        textArea = new JTextArea();

        skipButton.setSize(new Dimension(100,100));
        skipButton.setLocation(new Point(710,0));

        textArea.setSize(400,100);
        textArea.setLocation(new Point(810,35));
        textArea.setBackground(new Color(117, 148, 229));
        textArea.setFont(new Font("Consolas",Font.PLAIN,35));
        textArea.setEditable(false);

        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(100,100));
        panel.setBackground(new Color(117, 148, 229));
        panel.add(skipButton);
        panel.add(textArea);

        getContentPane().add(panel, BorderLayout.SOUTH);
        getContentPane().addMouseMotionListener(mover);
        getContentPane().addMouseListener(this);
        getContentPane().setBackground(new Color(117, 148, 229));

        setTitle("Trylma-WATD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }


    private void startGame() {
        new Thread(()->{
            try {
                while (isPlaying) {
                    DataPackage serverData = (DataPackage)packageReader.readObject();
                    String response = serverData.getServerResponse();
                    System.out.println(response);

                    if(response.equals("Valid")) {
                        int MPindex = serverData.getLiftedPawnIndex();
                        int Tindex = serverData.getDropTileIndex();
                        int STindex = serverData.getStartingTileIndex();
                        Point newLocation = tiles.get(Tindex).getCircleCenter();

                        tiles.get(STindex).leave();
                        tiles.get(Tindex).take();
                        movablePawns.get(MPindex).setCircleLocation(newLocation);
                        containsCircle = false;

                        textArea.setText("OPPONENT'S TURN");
                        panel.removeMouseListener(this);
                        getContentPane().removeMouseListener(this);
                        getContentPane().removeMouseMotionListener(mover);
                    }
                    if(response.equals("Valid & move")) {
                        int MPindex = serverData.getLiftedPawnIndex();
                        int Tindex = serverData.getDropTileIndex();
                        int STindex = serverData.getStartingTileIndex();
                        Point newLocation = tiles.get(Tindex).getCircleCenter();

                        tiles.get(STindex).leave();
                        tiles.get(Tindex).take();
                        movablePawns.get(MPindex).setCircleLocation(newLocation);
                        containsCircle = false;
                    }
                    if(response.equals("Update")) {
                        int Tindex = serverData.getDropTileIndex();
                        int STindex = serverData.getStartingTileIndex();
                        Point oldPawnL = serverData.getClientTiles().get(STindex).getCircleCenter();
                        Point newPawnL = serverData.getClientTiles().get(Tindex).getCircleCenter();

                        tiles.get(STindex).leave();
                        tiles.get(Tindex).take();

                        for(Pawn pawn : pawns) {
                            if(pawn.getCircleCenter().equals(oldPawnL)) {
                                pawn.setCircleLocation(newPawnL);
                            }
                        }

                        if(serverData.getCurrentPlayer()==startingArm) {
                            panel.addMouseListener(this);
                            getContentPane().addMouseListener(this);
                            getContentPane().addMouseMotionListener(mover);
                            textArea.setText("YOUR TURN");
                        } else {
                            textArea.setText("OPPONENT'S TURN");
                        }

                        containsCircle = false;
                    }
                    if(response.equals("Invalid")) {
                        int MPindex = serverData.getLiftedPawnIndex();
                        int STindex = serverData.getStartingTileIndex();

                        movablePawns.get(MPindex).setCircleLocation(tiles.get(STindex).getCircleCenter());
                        containsCircle = false;
                    }
                }
            } catch(IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void sendRequest(String command) throws IOException {
        DataPackage clientData = new DataPackage(tiles, pawns, movablePawns,winPoints);

        if(command.equals("Skip")) {
            clientData.setClientCommand(command);
        } else {
            clientData.setClientCommand(command);
            clientData.setCurrentPlayer(startingArm);
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

        for(int i = 0; i<printCount; i++) {

            int startingPointY = 460;
            int startingPointY2 = 220;
            Point point = new Point(x, (startingPointY + step));
            Point point2 = new Point(x, (startingPointY2 + step2));

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
        if(e.getSource()==skipButton) {
            try {
                sendRequest("Skip");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            temp = e.getPoint();

            for (int z = 0; z < movablePawns.size(); z++) {
                if ((movablePawns.get(z)).containsCircle(new Point(e.getX(), e.getY()))) {
                    System.out.println("TEST");

                    containsCircle = true;
                    Point savedPosition = new Point(movablePawns.get(z).getCircleCenter());
                    liftedPawnIndex = z;

                    for (int i = 0; i < tiles.size(); i++) {
                        if (tiles.get(i).getCircleCenter().equals(savedPosition)) {
                            System.out.println(savedPosition);
                            startingTileIndex = i;
                            System.out.println(startingTileIndex);
                            break;
                        }
                    }

                    getContentPane().remove(movablePawns.get(z));
                    getContentPane().add(movablePawns.get(z), 0);

                    return;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(containsCircle) {
            Point dropPoint = e.getPoint();

            for (int z = 0; z<tiles.size(); z++) {
                if ((tiles.get(z)).containsCircle(dropPoint)) {
                    dropTileIndex = z;
                    try {
                        sendRequest("Validate");
                        return;
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }

            movablePawns.get(liftedPawnIndex).setCircleLocation(tiles.get(startingTileIndex).getCircleCenter());
            containsCircle = false;
        }

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
        int aCount = 0;

        if(arm==startingArm){
            movablePawns.add(new Pawn(p,c));
            getContentPane().add(movablePawns.get(movablePawnsCounter),0);

            movablePawnsCounter++;
            aCount++;
        } else if(arm==winArm) {
            winPoints.add(p);
            pawns.add(new Pawn(p, c));
            getContentPane().add(pawns.get(pawnsCounter), 0);

            pawnsCounter++;
            aCount++;
        } else {
            pawns.add(new Pawn(p, c));
            getContentPane().add(pawns.get(pawnsCounter), 0);

            pawnsCounter++;
        }
        if(aCount>1) {
            System.out.println("UMMMMMMMMMMMMMMM?");
        }

        validate();
        repaint();
    }

    /**
     * Sets neighbour for every possible Tile on the board.
     */
    private void setNeighbours() {
        for(Tile tile: tiles) {
            int x1 = (int)tile.getCircleCenter().getX();
            int y1 = (int)tile.getCircleCenter().getY();

            for(Tile possibleNeighbour: tiles) {
                int x2 = (int)possibleNeighbour.getCircleCenter().getX();
                int y2 = (int)possibleNeighbour.getCircleCenter().getY();
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
                if(dx==-30 && dy==0) {
                    tile.setNeighbour(3, possibleNeighbour);
                }
                if(dx==-15 && dy==-30) {
                    tile.setNeighbour(4, possibleNeighbour);
                }
                if(dx==15 && dy==-30) {
                    tile.setNeighbour(5, possibleNeighbour);
                }
            }
        }
    }
}
