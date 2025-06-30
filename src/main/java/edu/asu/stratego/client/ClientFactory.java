package edu.asu.stratego.client;

import javafx.animation.Timeline;
import edu.asu.stratego.client.clientGameManager.clientGameManager.ClientGameInterface;
import edu.asu.stratego.client.clientGameManager.clientGameManager.ClientGameManager;
import edu.asu.stratego.client.clientGameManager.gameLogic.GameLogic;
import edu.asu.stratego.client.clientGameManager.gameLogic.GameLogicInterface;
import edu.asu.stratego.client.clientGameManager.gameUIManager.GameUIManager;
import edu.asu.stratego.client.clientGameManager.gameUIManager.GameUIManagerInterface;
import edu.asu.stratego.client.clientGameManager.serverConnection.ServerConnection;
import edu.asu.stratego.client.clientGameManager.serverConnection.ServerConnectionInterface;
import edu.asu.stratego.client.gui.ViewInterface;
import edu.asu.stratego.client.gui.board.setup.SelectPiece;
import edu.asu.stratego.client.gui.board.setup.SelectPieceInterface;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.SetupTimer;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.SetupTimerInterface;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.StartTimeInterface;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.StartTimer;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.TimerFinished;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.TimerFinishedInterface;

public class ClientFactory {

    public ClientGameInterface createClientGame(ViewInterface clientView) {
        // Crear las dependencias necesarias
        ServerConnectionInterface serverConnection = new ServerConnection();
        GameUIManagerInterface gameUIManager = new GameUIManager(clientView);
        GameLogicInterface gameLogic = new GameLogic(serverConnection,gameUIManager);
        TimerFinishedInterface timeFinishedHandler = new TimerFinished();
        StartTimeInterface startTimeHandler = new StartTimer(new Timeline());
        SetupTimerInterface setupTimer = new SetupTimer(timeFinishedHandler, startTimeHandler);
        SelectPieceInterface selectPiece = new SelectPiece();

        // Crear y devolver la instancia de ClientGameManager
        return new ClientGameManager(serverConnection, gameLogic, gameUIManager, setupTimer, selectPiece);
    }
}