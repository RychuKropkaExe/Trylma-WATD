package Client;

import javax.swing.*;
import java.awt.*;

/**
 * Creates little JFrame while waiting for all players to connect.
 */
public class WaitingScreen extends JFrame {

    public WaitingScreen() {
        JLabel label = new JLabel("Waiting for other players");
        setLayout(new FlowLayout());
        add(label);
        setSize(200, 100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //pack();
        setVisible(true);
    }
}
