package Server.Rules;

import Client.DataPackage;
import Client.Pawn;
import Client.Tile;

import javax.xml.crypto.Data;
import java.awt.*;
import java.util.ArrayList;

public class SimpleRules implements Rules{
    private ArrayList<Tile> clientTiles;
    private ArrayList<Pawn> clientPawns;
    private ArrayList<Pawn> clientMovablePawns;
    private ArrayList<Point> clientWinPoints;

    private Tile beetweenTile;

    private String clientCommand;
    private String serverResponse;

    private Point oldPawnLocation;
    private Point newPawnLocation;

    private int startingTileIndex;
    private int dropTileIndex;
    private int liftedPawnIndex;
    private boolean stillMove;

    private int validatedMPawnIndex;
    @Override
    public boolean checkMove(DataPackage dataPackage) {
        setBoardVariables(dataPackage);
        return validateMove();
    }

    @Override
    public boolean stillMove() {
        return stillMove;
    }
    @Override
    public void setBoardVariables(DataPackage data) {
        clientTiles = new ArrayList<>(data.getClientTiles());
        clientPawns = new ArrayList<>(data.getClientPawns());
        clientMovablePawns = new ArrayList<>(data.getClientMovablePawns());
        clientWinPoints = new ArrayList<>(data.getWinPoints());
        clientCommand = data.getClientCommand();
        startingTileIndex = data.getStartingTileIndex();
        dropTileIndex = data.getDropTileIndex();
        liftedPawnIndex = data.getLiftedPawnIndex();
    }

    @Override
    public boolean validateMove() {
        stillMove=false;
        Tile tile1 = clientTiles.get(startingTileIndex);
        Tile tile2 = clientTiles.get(dropTileIndex);
        if(tile2.isTaken()) {
            return false;
        }
        int dx = (int) (tile1.getCircleCenter().getX() - tile2.getCircleCenter().getX());
        int dy = (int) (tile1.getCircleCenter().getY() - tile2.getCircleCenter().getY());
        for(int i = 0; i<6;i++) {
            if(tile1.getNeighbour(i)!=null) {
                if (tile1.getNeighbour(i).getCircleCenter().equals(tile2.getCircleCenter())){
                    stillMove = false;
                    return !tile2.isTaken();
                }
            }
        }
        for(int i = 0; i<6;i++) {
            for(int j =0; j<6; j++) {
                if(tile1.getNeighbour(i)!=null && tile2.getNeighbour(j)!=null) {
                    int dx2 = (int)(tile1.getCircleCenter().getX() - tile2.getNeighbour(j).getCircleCenter().getX());
                    int dy2 = (int)(tile1.getCircleCenter().getY() - tile2.getNeighbour(j).getCircleCenter().getY());
                    if(tile1.getNeighbour(i).getCircleCenter().equals(tile2.getNeighbour(j).getCircleCenter())) {
                        if(dx==2*dx2 && dy==2*dy2 ) {
                            beetweenTile = tile1.getNeighbour(i);
                            stillMove = checkIfNextJumpIsPossible();
                            return tile1.getNeighbour(i).isTaken();
                        }
                    }
                }
            }
        }

        return false;
    }
    private boolean checkIfNextJumpIsPossible(){
        Tile tile1 = clientTiles.get(startingTileIndex);
        Tile tile2 = clientTiles.get(dropTileIndex);

        for(int i = 0; i<6; i++) {
            if(tile2.getNeighbour(i) != null) {
                if(tile2.getNeighbour(i).isTaken() && !tile2.getNeighbour(i).getCircleCenter().equals(beetweenTile.getCircleCenter())) {
                    Tile possibleJump =  tile2.getNeighbour(i);
                    int dx = (int)(tile2.getCircleCenter().getX() - possibleJump.getCircleCenter().getX());
                    int dy = (int)(tile2.getCircleCenter().getY() - possibleJump.getCircleCenter().getY());
                    for(int j = 0; j<6 ; j++) {
                        if(possibleJump.getNeighbour(j) != null) {
                            int dx2 = (int)(tile2.getCircleCenter().getX() - possibleJump.getNeighbour(j).getCircleCenter().getX());
                            int dy2 = (int)(tile2.getCircleCenter().getY() - possibleJump.getNeighbour(j).getCircleCenter().getY());
                            if(2*dx==dx2 && 2*dy==dy2) {
                                return !possibleJump.getNeighbour(j).isTaken();
                            }
                        }
                    }
                }
            }
        }
        return false;

    }

}
