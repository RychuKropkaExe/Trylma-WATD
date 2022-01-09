package Client;

public class tester {
    public static void main(String[] args) {
        Boolean[] starArm = new Boolean[6];
        starArm[0] = false;
        starArm[1] = true;
        starArm[2] = true;
        starArm[3] = false;
        starArm[4] = true;
        starArm[5] = true;
        new Board(starArm, 2);
    }
}
