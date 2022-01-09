package Client;

import javax.swing.*;
import java.awt.*;

public class WaitingRoom extends JFrame {

    public WaitingRoom() {
        setTitle("Waiting room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(600, 400);
        setLayout(new FlowLayout());

        JButton submitButton = new JButton("Test");
        add(submitButton);

        setVisible(true);
    }
}
