package Client;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Creates packages with data to send between Client and Server
 * that contains actual info about state of the game.
 */
public class DataPackage implements Serializable {


    /** Arraylists with Tiles and Pawns used to play with */
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


    /**
     * Creates new Package with game info.
     *
     * @param tiles  Arraylist of Board Tiles
     * @param pawns  Arraylist of all Players Pawns
     * @param movablePawns  Arraylist of given Player Pawns
     * @param winPoints  Arraylist of given Player winning Tiles
     */
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
