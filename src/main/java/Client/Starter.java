package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Starter extends JFrame implements ActionListener {


    private static Socket socket;
    private static String message;
    private static BufferedReader input;
    private static PrintWriter output;

    private static final int PORT = 9090;

    private final String[] lobbySizes = {"Select number of players","2","3","4","6"};
    private final JComboBox<String> comboBox;

    private final JCheckBox arm1 = new JCheckBox();
    private final JCheckBox arm2 = new JCheckBox();
    private final JCheckBox arm3 = new JCheckBox();
    private final JCheckBox arm4 = new JCheckBox();
    private final JCheckBox arm5 = new JCheckBox();
    private final JCheckBox arm6 = new JCheckBox();

    private final JButton submitButton = new JButton("Submit");


    public Starter() {
        arm1.setText("Draw bottom arm");
        arm2.setText("Draw bottom left arm");
        arm3.setText("Draw top left arm");
        arm4.setText("Draw top arm");
        arm5.setText("Draw top right arm");
        arm6.setText("Draw bottom right arm");

        arm1.setFocusable(true);
        arm2.setFocusable(true);
        arm3.setFocusable(true);
        arm4.setFocusable(true);
        arm5.setFocusable(true);
        arm6.setFocusable(true);

        comboBox = new JComboBox<>(lobbySizes);
        comboBox.addActionListener(this);

        submitButton.addActionListener(this);

        setTitle("Starter lobby");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(600, 400);
        setLayout(new FlowLayout());

        add(arm1);
        add(arm2);
        add(arm3);
        add(arm4);
        add(arm5);
        add(arm6);
        add(comboBox);
        add(submitButton);

        setVisible(true);
    }

    public static void listener() {
        new Thread(() -> {
            while(socket.isConnected()) {
                try {
                    message = input.readLine();
                    if (message.equals("[Server]Choose number of players")) {
                        new Starter();
                    } else if (message.equals("[Server] You have joined the lobby!")) {
                        System.out.println("You have joined");
                    } else if (message.equals("A player has joined the lobby")) {
                        System.out.println("New player has joined");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==submitButton) {
            if(comboBox.getSelectedItem()!="Select number of players") {
                String[] arms = new String[6];

                String selected = (String) comboBox.getSelectedItem();
                arms[0] = String.valueOf(arm1.isSelected());
                arms[1] = String.valueOf(arm2.isSelected());
                arms[2] = String.valueOf(arm3.isSelected());
                arms[3] = String.valueOf(arm4.isSelected());
                arms[4] = String.valueOf(arm5.isSelected());
                arms[5] = String.valueOf(arm6.isSelected());
                output.println(selected);

                for (int i = 0; i < 6; i++) {
                    System.out.println(arms[i]);
                    output.println(arms[i]);
                    output.flush();
                }

                System.out.println("Started");
                dispose();

                new WaitingRoom();
            }
        }
    }

    public static void main(String[] args) throws IOException {

        socket = new Socket("localhost",PORT);
        output = new PrintWriter( new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println(socket);

        listener();

        while(socket.isConnected()) {
        }
    }
}
