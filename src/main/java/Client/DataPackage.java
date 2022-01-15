package Client;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class DataPackage implements Serializable {


    private final ArrayList<Tile> clientTiles;
    private final ArrayList<Pawn> clientPawns;
    private final ArrayList<Pawn> clientMovablePawns;
    private final ArrayList<Point> clientWinPoints;

    private String clientCommand;
    private String serverResponse;

    private int startingTileIndex;
    private int dropTileIndex;
    private int liftedPawnIndex;
    private int currentPlayer;

    private boolean skipFlag;


    public DataPackage(ArrayList<Tile> tiles, ArrayList<Pawn> pawns, ArrayList<Pawn> movablePawns, ArrayList<Point> winPoints) {
        clientTiles = tiles;
        clientPawns = pawns;
        clientMovablePawns = movablePawns;
        clientWinPoints = winPoints;
    }

    public ArrayList<Tile> getClientTiles() {
        return clientTiles;
    }

    public ArrayList<Point> getWinPoints() {
        return clientWinPoints;
    }

    public ArrayList<Pawn> getClientMovablePawns() {
        return clientMovablePawns;
    }

    public ArrayList<Pawn> getClientPawns() {
        return clientPawns;
    }

    public String getClientCommand() {
        return clientCommand;
    }

    public void setClientCommand(String clientCommand) {
        this.clientCommand = clientCommand;
    }

    public String getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(String serverResponse) {
        this.serverResponse = serverResponse;
    }

    public int getStartingTileIndex() {
        return startingTileIndex;
    }

    public void setStartingTileIndex(int startingTileIndex) {
        this.startingTileIndex = startingTileIndex;
    }

    public int getDropTileIndex() {
        return dropTileIndex;
    }

    public void setDropTileIndex(int dropTileIndex) {
        this.dropTileIndex = dropTileIndex;
    }

    public int getLiftedPawnIndex() {
        return liftedPawnIndex;
    }

    public void setLiftedPawnIndex(int liftedPawnIndex) {
        this.liftedPawnIndex = liftedPawnIndex;
    }

    public boolean getSkipFlag() {
        return skipFlag;
    }

    public void setSkipFlag(boolean skipFlag) {
        this.skipFlag = skipFlag;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

}
