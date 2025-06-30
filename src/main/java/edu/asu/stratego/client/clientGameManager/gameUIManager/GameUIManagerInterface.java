package edu.asu.stratego.client.clientGameManager.gameUIManager;

import edu.asu.stratego.client.clientGameManager.serverConnection.ServerConnectionInterface;
import edu.asu.stratego.client.gui.board.setup.SelectPieceInterface;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.SetupTimerInterface;

public interface GameUIManagerInterface {
    public void connectToServer();
    public void waitForOpponent(ServerConnectionInterface serverConnectionInterface);
    public void setupBoard(ServerConnectionInterface serverConnectionInterface,SetupTimerInterface setupTimer, SelectPieceInterface selectPiece);
    public void showFinalScene(String winner, String loser, ServerConnectionInterface serverConnection);
}
