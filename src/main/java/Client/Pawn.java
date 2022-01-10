package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class Pawn extends JComponent {


    private Point CircleCenter;
    private Color circleColor;
    private final int CircleRadius;


    public Pawn(Point a, Color c) {
        CircleRadius = 25;
        circleColor = c;
        CircleCenter = new Point((int)a.getX(), (int)a.getY());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawingCircle(g);
    }

    private void doDrawingCircle(Graphics g) {
        Graphics2D c2D = (Graphics2D) g;
        Ellipse2D pawn = new Ellipse2D.Float(CircleCenter.x,CircleCenter.y,CircleRadius,CircleRadius);

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        c2D.setRenderingHints(rh);

        c2D.setColor(Color.BLACK);
        c2D.setStroke(new BasicStroke(2));
        c2D.draw(pawn);
        c2D.setColor(circleColor);
        c2D.fill(pawn);
    }

    public void setColor(Color c) {
        circleColor = c;
        repaint();
    }

    public boolean containsCircle(Point a){
        return (new Ellipse2D.Float(CircleCenter.x,CircleCenter.y,CircleRadius,CircleRadius).contains(a));
    }

    public void translateCircle(Point CircleP, MouseEvent e){
        CircleCenter = new Point(CircleP.x+CircleCenter.x,CircleP.y+CircleCenter.y);
        revalidate();
        repaint();
    }

    public void setCircleLocation(Point p) {
        CircleCenter = p;
        revalidate();
        repaint();
    }

    public Point getCircleCenter() {
        return CircleCenter;
    }
}
