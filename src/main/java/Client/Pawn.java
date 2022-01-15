package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

/**
 * Creates and draws Pawns
 */
public class Pawn extends JComponent {


    private final Color circleColor;
    private final int circleRadius;
    private Point circleCenter;


    /**
     * Sets Pawn parameters.
     *
     * @param a  Point containing center of the Pawn coordinates
     * @param c  Pawn Color
     */
    public Pawn(Point a, Color c) {
        circleRadius = 25;
        circleColor = c;
        circleCenter = new Point((int)a.getX(), (int)a.getY());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawingCircle(g);
    }

    /**
     * Draws the Pawn with Border and Antialiasing.
     */
    private void doDrawingCircle(Graphics g) {
        Graphics2D c2D = (Graphics2D) g;
        Ellipse2D pawn = new Ellipse2D.Float(circleCenter.x, circleCenter.y, circleRadius, circleRadius);

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        c2D.setRenderingHints(rh);

        c2D.setColor(Color.BLACK);
        c2D.setStroke(new BasicStroke(2));
        c2D.draw(pawn);
        c2D.setColor(circleColor);
        c2D.fill(pawn);
    }

    /**
     * Checks whether given Point is inside any Pawn.
     *
     * @param point  Checked Point
     * @return  True when contains, false if not
     */
    public boolean containsCircle(Point point){
        return (new Ellipse2D.Float(circleCenter.x, circleCenter.y, circleRadius, circleRadius).contains(point));
    }

    /**
     * Changes the center Point of the Pawn to new value.
     *
     * @param circleP  New center Point
     */
    public void translateCircle(Point circleP, MouseEvent e){
        circleCenter = new Point(circleP.x+circleCenter.x,circleP.y+circleCenter.y);
        revalidate();
        repaint();
    }

    public void setCircleLocation(Point p) {
        circleCenter = p;
        revalidate();
        repaint();
    }

    public Point getCircleCenter() {
        return circleCenter;
    }
}
