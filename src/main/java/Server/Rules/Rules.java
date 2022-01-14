package Server.Rules;


import Client.DataPackage;

public interface Rules{
    boolean checkMove(DataPackage dataPackage);
    boolean stillMove();
    void setBoardVariables(DataPackage data);
    boolean validateMove();
}
