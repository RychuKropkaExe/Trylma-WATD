package Client;

public class Tester {
    public static void main(String[] args) {

        Boolean[] starArm = new Boolean[6];
        starArm[0] = true;
        starArm[1] = true;
        starArm[2] = true;
        starArm[3] = true;
        starArm[4] = true;
        starArm[5] = true;

        new Board(starArm, 2);
    }
}
