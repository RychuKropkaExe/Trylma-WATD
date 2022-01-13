package Server.Rules;


import Client.DataPackage;

public interface Rules{
    boolean checkMove(DataPackage dataPackage);
    void getBoard();
    void setBoardVariables(DataPackage data);
    boolean validateMove();
}
