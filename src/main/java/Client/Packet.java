package Client;

import java.io.Serializable;

public class Packet implements Serializable {


    public String command;
    public int num;


    public Packet(String cm) {
        command = cm;
    }

    public Packet(int n) {
        num = n;
    }
}
