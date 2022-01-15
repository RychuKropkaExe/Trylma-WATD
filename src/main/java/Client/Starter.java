package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

/**
 * Manages clients instances deciding whether to create lobby or waiting rooms.
 */
public class Starter extends JFrame implements ActionListener {


    private Connector connector;

    private JComboBox<String> comboBox;
    private final JButton submitButton = new JButton("Submit");

    private final JCheckBox arm1 = new JCheckBox();
    private final JCheckBox arm2 = new JCheckBox();
    private final JCheckBox arm3 = new JCheckBox();
    private final JCheckBox arm4 = new JCheckBox();
    private final JCheckBox arm5 = new JCheckBox();
    private final JCheckBox arm6 = new JCheckBox();

    private static WaitingScreen screen;

    public static int playersNumber;


    public Starter(Connector connector) throws IOException {
        this.connector = connector;

        listener();
    }


    /**
     * Creates JFrame with game settings.
     * Available only for the first player.
     */
    private void initFrame(){
        String[] lobbySizes = {"Select number of players", "2", "3", "4", "6"};

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
        setSize(300, 200);
        setLocationRelativeTo(null);
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

    /**
     * Creates new Thread for each new Player and decides which type of Client shall be opened.
     */
    public void listener() {
        new Thread(() -> {
            while(connector.getSocket().isConnected()) {
                try {
                    Packet a = (Packet)connector.getInput().readObject();
                    String message = a.command;

                    System.out.println(a.command);

                    if(message.equals("[Server]Choose number of players")) {
                        initFrame();
                    } else if (message.equals("[Server] You have joined the lobby!")) {
                        System.out.println("You have joined");
                        screen = new WaitingScreen();
                    } else if (message.equals("A player has joined the lobby")) {
                        System.out.println("New player has joined");
                    } else if(message.equals("The game is starting!")) {
                        System.out.println("Umm gucci?");
                        screen.dispose();
                        System.out.println("WIDZISZ MNIE?");
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                new Board(getArms(), getPlayerID(), getPlayers(),
                          getWinArm(), getStartingPlayer(), connector);
                connector = null;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Decides which Board Arms shall be drawn depending of number of players.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==submitButton) {
            if(comboBox.getSelectedItem()!="Select number of players") {
                String[] arms = new String[6];
                playersNumber = Integer.parseInt((String) Objects.requireNonNull(comboBox.getSelectedItem()));

                arms[0] = String.valueOf(arm1.isSelected());
                arms[1] = String.valueOf(arm2.isSelected());
                arms[2] = String.valueOf(arm3.isSelected());
                arms[3] = String.valueOf(arm4.isSelected());
                arms[4] = String.valueOf(arm5.isSelected());
                arms[5] = String.valueOf(arm6.isSelected());

                if(playersNumber==2) {
                    arms[0] = String.valueOf(true);
                    arms[3] = String.valueOf(true);
                }
                if(playersNumber==3) {
                    arms[0] = String.valueOf(true);
                    arms[1] = String.valueOf(true);
                    arms[2] = String.valueOf(true);
                    arms[3] = String.valueOf(true);
                    arms[4] = String.valueOf(true);
                    arms[5] = String.valueOf(true);
                }
                if(playersNumber==4) {
                    arms[1] = String.valueOf(true);
                    arms[2] = String.valueOf(true);
                    arms[4] = String.valueOf(true);
                    arms[5] = String.valueOf(true);
                }
                if(playersNumber==6) {
                    arms[0] = String.valueOf(true);
                    arms[1] = String.valueOf(true);
                    arms[2] = String.valueOf(true);
                    arms[3] = String.valueOf(true);
                    arms[4] = String.valueOf(true);
                    arms[5] = String.valueOf(true);
                }
                try {
                    Packet packet = new Packet(playersNumber);

                    connector.getOutput().writeObject(packet);
                    connector.getOutput().flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                for (int i = 0; i < 6; i++) {
                    System.out.println(arms[i]);
                    Packet packet = new Packet(String.valueOf(arms[i]));

                    try {
                        connector.getOutput().writeObject(packet);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    try {
                        connector.getOutput().flush();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                System.out.println(playersNumber);
                System.out.println("Started");

                dispose();
                screen = new WaitingScreen();
            }
        }
    }

    /**
     * Returns which Board Arms shall be drawn.
     */
    private Boolean[] getArms() throws IOException, ClassNotFoundException {
        System.out.println("DOCHODZIMY TUTAJ");
        Boolean[] temp = new Boolean[6];

        for(int i = 0; i<6; i++) {
            Packet packet = (Packet)connector.getInput().readObject();
            temp[i] = Boolean.parseBoolean(packet.command);
        }

        return temp;
    }


    private int getStartingPlayer() throws IOException, ClassNotFoundException {
        Packet packet = (Packet) connector.getInput().readObject();
        return packet.num;
    }

    private int getPlayerID() throws IOException, ClassNotFoundException {
        Packet packet = (Packet) connector.getInput().readObject();
        return packet.num;
    }
    private int getWinArm() throws IOException, ClassNotFoundException {
        Packet packet = (Packet) connector.getInput().readObject();
        return packet.num;
    }

    private int getPlayers() throws IOException, ClassNotFoundException {
        Packet packet = (Packet) connector.getInput().readObject();
        return packet.num;
    }
}
