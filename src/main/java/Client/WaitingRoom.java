package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class WaitingRoom extends JFrame {


    private static Socket socket;
    private static BufferedReader input;
    private static PrintWriter output;

    private JCheckBox readyCheck;

    private static final int PORT = 9090;
    private int readyCounter;


    public WaitingRoom() throws IOException {

        //establishConnection();

        for(int i=0; i<Starter.playersNumber; i++) {
            JFrame newFrame = new JFrame();
            createFrame(newFrame,i+1);
        }
    }

    private void createFrame(JFrame frame, int i) {
        readyCheck = new JCheckBox("Ready");
        JLabel label = new JLabel("Number of players ready: " + readyCounter + "/" + Starter.playersNumber);

        frame.setTitle("Waiting room" + i);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocation(i*200, i*100);
        frame.setLayout(new FlowLayout());

        frame.add(readyCheck);
        frame.add(label);

        readyCheck.addActionListener(e -> {
            if(readyCheck.isSelected()) {
                readyCounter++;
                frame.getContentPane().setBackground(Color.BLACK);
            } else {
                readyCounter--;
                frame.getContentPane().setBackground(Color.GREEN);
            }
            label.setText("Number of players ready: " + readyCounter + "/" + Starter.playersNumber);
        });

        frame.setVisible(true);
    }

    private void establishConnection() throws IOException {
        socket = new Socket("localhost", PORT);
        output = new PrintWriter( new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }
}
