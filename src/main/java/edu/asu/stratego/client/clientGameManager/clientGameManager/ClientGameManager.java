package edu.asu.stratego.client.clientGameManager.clientGameManager;

import edu.asu.stratego.client.clientGameManager.gameLogic.GameLogicInterface;
import edu.asu.stratego.client.clientGameManager.gameUIManager.GameUIManagerInterface;
import edu.asu.stratego.client.clientGameManager.serverConnection.ServerConnectionInterface;
import edu.asu.stratego.client.gui.board.setup.SelectPieceInterface;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.SetupTimerInterface;

public class ClientGameManager implements ClientGameInterface {
    private final ServerConnectionInterface serverConnectionInterface;
    private final GameLogicInterface gameLogicInterface;
    private final GameUIManagerInterface gameUIManagerInterface;
    private final SetupTimerInterface setupTimer;
    private final SelectPieceInterface selectPiece;
    public static boolean RANDOM_SETUP = false;

    public ClientGameManager(ServerConnectionInterface serverConnection, GameLogicInterface gameLogic, GameUIManagerInterface gameUIManager,SetupTimerInterface setupTimer, SelectPieceInterface selectPiece) {
        this.serverConnectionInterface = serverConnection;
        this.gameLogicInterface = gameLogic;
        this.gameUIManagerInterface = gameUIManager;
        this.setupTimer = setupTimer;
        this.selectPiece = selectPiece;
    }

    @Override
    public void run() {
        gameUIManagerInterface.connectToServer();
        gameUIManagerInterface.waitForOpponent(serverConnectionInterface);
        gameUIManagerInterface.setupBoard(serverConnectionInterface,setupTimer,selectPiece);
        gameLogicInterface.playGame();
    }
}