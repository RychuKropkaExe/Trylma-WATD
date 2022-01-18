package Server.Rules;

import Client.*;


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
    private Point validatedPawnLocation;

    private int startingTileIndex;
    private int dropTileIndex;
    private int liftedPawnIndex;

    private boolean stillMove;
    private boolean jumpFlag;
    private boolean basePoint = false;
    private boolean isWinning = false;
    private boolean isBlocking = false;

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
        basePoint = false;
        jumpFlag = data.getJumpFlag();
        validatedPawnLocation = data.getValidatedPawnLocation();
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
        if(jumpFlag && !validatedPawnLocation.equals(clientTiles.get(startingTileIndex).getCircleCenter())){
            stillMove=true;
            return false;
        }
        if(tile2.isTaken()) {
            return false;
        }
        for(Point point : clientWinPoints) {
            if(point.getLocation().equals(clientTiles.get(startingTileIndex).getCircleCenter())) {
                basePoint=true;
                break;
            }
        }
        //jumping a single tile
        int dx = (int) (tile1.getCircleCenter().getX() - tile2.getCircleCenter().getX());
        int dy = (int) (tile1.getCircleCenter().getY() - tile2.getCircleCenter().getY());
        if(!jumpFlag) {
            for (int i = 0; i < 6; i++) {
                if (tile1.getNeighbour(i) != null) {
                    if (tile1.getNeighbour(i).getCircleCenter().equals(tile2.getCircleCenter())) {
                        stillMove = false;
                        if(basePoint) {
                            return  !tile2.isTaken() && checkIfMoveIsInBase();
                        } else if(checkIfMoveIsInBase()) {
                            if(!tile2.isTaken()) {
                                isWinning = checkIfMoveIsWinning();
                                isBlocking = checkIfBlock();
                                return true;
                            }
                        } else {
                            return !tile2.isTaken();
                        }
                    }
                }
            }
        }
        //jumping two tiles
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    if (tile1.getNeighbour(i) != null && tile2.getNeighbour(j) != null) {
                        int dx2 = (int) (tile1.getCircleCenter().getX() - tile2.getNeighbour(j).getCircleCenter().getX());
                        int dy2 = (int) (tile1.getCircleCenter().getY() - tile2.getNeighbour(j).getCircleCenter().getY());
                        if (tile1.getNeighbour(i).getCircleCenter().equals(tile2.getNeighbour(j).getCircleCenter())) {
                            if (dx == 2 * dx2 && dy == 2 * dy2) {
                                beetweenTile = tile1.getNeighbour(i);
                                stillMove = checkIfNextJumpIsPossible();
                                if(basePoint) {
                                    return tile1.getNeighbour(i).isTaken() && checkIfMoveIsInBase();
                                } else if(checkIfMoveIsInBase()) {
                                    if(!tile1.getNeighbour(i).isTaken()) {
                                        isWinning = checkIfMoveIsWinning();
                                        isBlocking = checkIfBlock();
                                        return true;
                                    }
                                } else {
                                    return tile1.getNeighbour(i).isTaken();
                                }
                            }
                        }
                    }
                }
            }

        return false;
    }

    private boolean checkIfMoveIsWinning() {
        int size = clientWinPoints.size();
        int counter = 0;
        clientMovablePawns.get(liftedPawnIndex).setCircleLocation(clientTiles.get(dropTileIndex).getCircleCenter());
        for (Pawn pawn : clientMovablePawns) {
            for (Point point : clientWinPoints) {
                if (pawn.getCircleCenter().equals(point)) {
                    counter++;
                    break;
                }
            }
        }
        if (counter == size) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBlocking() {
        return isBlocking;
    }
    public boolean isWinning() {
        return isWinning;
    }

    private boolean checkIfBlock() {
        clientMovablePawns.get(liftedPawnIndex).setCircleLocation(clientTiles.get(dropTileIndex).getCircleCenter());
        for(Point point: clientWinPoints) {
            for(Tile tile: clientTiles) {
                int counter=0;
                boolean canBlock = false;
                if(tile.getCircleCenter().equals(point)) {
                    for(int i = 0; i<6;i++) {
                        if(tile.getNeighbour(i)!=null) {
                            if(clientWinPoints.contains(tile.getNeighbour(i).getCircleCenter())) {
                                for(Pawn pawn: clientMovablePawns) {
                                    if (tile.getNeighbour(i).getCircleCenter().equals(pawn.getCircleCenter())) {
                                        counter++;
                                        break;
                                    }
                                }
                            } else {
                                canBlock = true;
                            }
                        }
                    }
                    if(counter>=4 && canBlock) {
                        return true;
                        /*int block = clientWinPoints.size();
                        for(Tile tile2 : clientTiles) {
                            for(Point winPoint: clientWinPoints) {
                                if(tile2.getCircleCenter().equals(winPoint)) {
                                    if(tile2.isTaken) {
                                        block--;
                                        break;
                                    }
                                }
                            }
                        }
                        if(block==0) {
                            return true;
                        } else {
                            return false;
                        }*/
                    }
                }
            }
        }
        return false;
    }

    private boolean checkIfMoveIsInBase() {
        Tile tile2 = clientTiles.get(dropTileIndex);
        for(Point point: clientWinPoints) {
            if(tile2.getCircleCenter().equals(point)) {
                return true;
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
