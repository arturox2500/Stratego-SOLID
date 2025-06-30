package edu.asu.stratego.server;

import java.net.Socket;

import edu.asu.stratego.server.serverGameManager.boardManager.BoardManager;
import edu.asu.stratego.server.serverGameManager.boardManager.BoardManagerInterface;
import edu.asu.stratego.server.serverGameManager.clientCommunication.ClientCommunicationManager;
import edu.asu.stratego.server.serverGameManager.gameFlow.GameFlowInterface;
import edu.asu.stratego.server.serverGameManager.gameFlow.GameFlowManager;
import edu.asu.stratego.server.serverGameManager.moveValidator.MoveValidator;
import edu.asu.stratego.server.serverGameManager.moveValidator.MoveValidatorInterface;
import edu.asu.stratego.server.serverGameManager.playerManager.PlayerManager;
import edu.asu.stratego.server.serverGameManager.playerManager.PlayerManagerInterface;
import edu.asu.stratego.server.serverGameManager.serverGameManager.ServerGameInterface;
import edu.asu.stratego.server.serverGameManager.serverGameManager.ServerGameManager;

public class GameSessionFactory {

    public ServerGameInterface createSession(Socket playerOne, Socket playerTwo, String sessionName) {
        // Crear las dependencias necesarias para la sesión
        ClientCommunicationManager communicationManager = new ClientCommunicationManager(playerOne, playerTwo);
        PlayerManagerInterface playerManager = new PlayerManager(communicationManager);
        BoardManagerInterface boardManager = new BoardManager(communicationManager);
        MoveValidatorInterface moveValidator = new MoveValidator(boardManager.getBoard());
        GameFlowInterface gameFlowManager = new GameFlowManager(communicationManager, playerManager, boardManager, sessionName, moveValidator);
        boardManager.setGameFlow(gameFlowManager);

        // Crear y devolver la instancia de ServerGameManager
        return new ServerGameManager(communicationManager, playerManager, boardManager, gameFlowManager);
    }
}
