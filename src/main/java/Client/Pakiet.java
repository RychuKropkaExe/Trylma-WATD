package Client;

import java.io.Serializable;

public class Pakiet implements Serializable {
    public String command;
    public int num;
    public Pakiet(String cm) {
        command = cm;
    }
    public Pakiet(int n) {
        num = n;
    }
}
