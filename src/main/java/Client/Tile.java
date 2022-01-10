package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Tile extends JComponent {


    private final Tile[] neighbours = new Tile[6];
    private final Point CircleCenter;
    private final Color circleColor;
    private final int CircleRadius;

    public boolean isTaken = false;


    public Tile(Point a) {
        CircleRadius = 25;
        circleColor = new Color(188,188,188);
        CircleCenter = new Point((int)a.getX(), (int)a.getY());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawingCircle(g);
    }

    private void doDrawingCircle(Graphics g) {
        Graphics2D c2D = (Graphics2D) g;
        Ellipse2D tile = new Ellipse2D.Float(CircleCenter.x,CircleCenter.y,CircleRadius,CircleRadius);

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        c2D.setRenderingHints(rh);

        c2D.setColor(Color.BLACK);
        c2D.setStroke(new BasicStroke(3));
        c2D.draw(tile);
        c2D.setColor(circleColor);
        c2D.fill(tile);
    }

    public void setNeighbour(int index, Tile tile) {
        neighbours[index] = tile;
    }

    public Tile getNeighbour(int index) {
        return neighbours[index];
    }

    public Color getCircleColor() {
        return circleColor;
    }

    public void take() {
        isTaken = true;
    }

    public void leave() {
        isTaken = false;
    }

    public boolean containsCircle(Point point) {
        return (new Ellipse2D.Float(CircleCenter.x,CircleCenter.y,CircleRadius,CircleRadius).contains(point));
    }

    public Point getCircleCenter() {
        return CircleCenter;
    }
}
