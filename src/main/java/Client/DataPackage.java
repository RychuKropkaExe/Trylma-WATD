package Client;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class DataPackage implements Serializable {


    private ArrayList<Tile> clientTiles;
    private ArrayList<Pawn> clientPawns;
    private ArrayList<Pawn> clientMovablePawns;
    private ArrayList<Point> clientWinPoints;

    private String clientCommand;
    private String serverResponse;

    private Point oldPawnLocation;
    private Point newPawnLocation;
    private Point validatedPawnLocation;

    private int startingTileIndex;
    private int dropTileIndex;
    private int liftedPawnIndex;
    private int validatedMPawnIndex;
    private int currentPlayer;
    private int placeCounter;

    private boolean skipFlag;
    private boolean jumpFlag;
    private boolean isWinning;
    private boolean exitFlag;




    public DataPackage(ArrayList<Tile> tiles, ArrayList<Pawn> pawns, ArrayList<Pawn> movablePawns, ArrayList<Point> winPoints) {
        clientTiles = tiles;
        clientPawns = pawns;
        clientMovablePawns = movablePawns;
        clientWinPoints = winPoints;
    }

    public boolean isExitFlag() {
        return exitFlag;
    }

    public void setExitFlag(boolean exitFlag) {
        this.exitFlag = exitFlag;
    }

    public boolean isWinning() {
        return isWinning;
    }

    public void setWinning(boolean winning) {
        isWinning = winning;
    }

    public int getPlaceCounter() {
        return placeCounter;
    }

    public void setPlaceCounter(int placeCounter) {
        this.placeCounter = placeCounter;
    }

    public boolean getJumpFlag() {
        return jumpFlag;
    }

    public void setJumpFlag(boolean jumpFlag) {
        this.jumpFlag = jumpFlag;
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

    public Point getValidatedPawnLocation() {
        return validatedPawnLocation;
    }

    public void setValidatedPawnLocation(Point validatedPawnLocation) {
        this.validatedPawnLocation = validatedPawnLocation;
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

    public int getValidatedMPawnIndex() {
        return validatedMPawnIndex;
    }

    public void setValidatedMPawnIndex(int validatedMPawnIndex) {
        this.validatedMPawnIndex = validatedMPawnIndex;
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

    public Point getNewPawnLocation() {
        return newPawnLocation;
    }

    public void setNewPawnLocation(Point newPawnLocation) {
        this.newPawnLocation = newPawnLocation;
    }

    public Point getOldPawnLocation() {
        return oldPawnLocation;
    }

    public void setOldPawnLocation(Point oldPawnLocation) {
        this.oldPawnLocation = oldPawnLocation;
    }

    public int getLiftedPawnIndex() {
        return liftedPawnIndex;
    }

    public void setLiftedPawnIndex(int liftedPawnIndex) {
        this.liftedPawnIndex = liftedPawnIndex;
    }

}
