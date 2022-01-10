package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Tile extends JComponent {


    private final Tile[] neighbours = new Tile[6];

    private final Point circleCenter;
    private final Color circleColor;
    private final int circleRadius;

    /** Variable containing state of the Tile */
    public boolean isTaken = false;

    /**
     * Sets Tile parameters.
     *
     * @param a  Point containing center of the Pawn coordinates
     */
    public Tile(Point a) {
        circleRadius = 25;
        circleColor = new Color(188,188,188);
        circleCenter = new Point((int)a.getX(), (int)a.getY());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawingCircle(g);
    }

    /**
     * Draws the Tile with Border and Antialiasing.
     */
    private void doDrawingCircle(Graphics g) {
        Graphics2D c2D = (Graphics2D) g;
        Ellipse2D tile = new Ellipse2D.Float(circleCenter.x, circleCenter.y, circleRadius, circleRadius);

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        c2D.setRenderingHints(rh);

        c2D.setColor(Color.BLACK);
        c2D.setStroke(new BasicStroke(3));
        c2D.draw(tile);
        c2D.setColor(circleColor);
        c2D.fill(tile);
    }

    /**
     * Sets particular neighbour of the given Tile.
     *
     * @param index  Certain neighbour index
     * @param tile  Targeted Tile which method sets the neighbour for
     */
    public void setNeighbour(int index, Tile tile) {
        neighbours[index] = tile;
    }

    /**
     * Returns particular neighbour of the Tile.
     *
     * @param index  Neighbour index
     */
    public Tile getNeighbour(int index) {
        return neighbours[index];
    }

    public Color getCircleColor() {
        return circleColor;
    }

    /**
     * Changes the state of Tile to be taken, preventing Pawns to be moved onto it.
     */
    public void take() {
        isTaken = true;
    }

    /**
     * Changes the state of Tile to be free, thus letting Pawns to be moved onto it
     */
    public void leave() {
        isTaken = false;
    }

    /**
     * Checks whether given Point is inside any Tile.
     *
     * @param point  Checked Point
     * @return  True when contains, false if not
     */
    public boolean containsCircle(Point point) {
        return (new Ellipse2D.Float(circleCenter.x, circleCenter.y, circleRadius, circleRadius).contains(point));
    }

    public Point getCircleCenter() {
        return circleCenter;
    }
}
