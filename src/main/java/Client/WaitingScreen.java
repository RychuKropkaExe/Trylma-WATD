package Client;

import javax.swing.*;
import java.awt.*;

/**
 * Creates little JFrame while waiting for all players to connect.
 */
public class WaitingScreen extends JFrame {

    public WaitingScreen() {
        JLabel label = new JLabel("Waiting for other players");

        setTitle("Waiting");
        setSize(200, 100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(label);
        setVisible(true);
    }
}
