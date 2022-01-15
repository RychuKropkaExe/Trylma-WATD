package Client;

import java.io.Serializable;

/**
 * Class used for creating simple packages with String or Int data
 * used for communication before game initializes.
 */
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
