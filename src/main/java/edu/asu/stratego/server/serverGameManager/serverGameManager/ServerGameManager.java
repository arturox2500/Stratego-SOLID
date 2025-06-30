package edu.asu.stratego.server.serverGameManager.serverGameManager;

import edu.asu.stratego.server.serverGameManager.Component;
import edu.asu.stratego.server.serverGameManager.boardManager.BoardManagerInterface;
import edu.asu.stratego.server.serverGameManager.clientCommunication.Mediator;
import edu.asu.stratego.server.serverGameManager.gameFlow.GameFlowInterface;
import edu.asu.stratego.server.serverGameManager.playerManager.PlayerManagerInterface;

public class ServerGameManager extends Component implements ServerGameInterface {
    private final PlayerManagerInterface playerManagerInterface;
    private final BoardManagerInterface boardManagerInterface;
    private final GameFlowInterface gameFlowInterface;

    public ServerGameManager(Mediator clientCommunication,
            PlayerManagerInterface playerManager,
            BoardManagerInterface boardManager,
            GameFlowInterface gameFlowManager) {
        this.mediator = clientCommunication;
        this.playerManagerInterface = playerManager;
        this.boardManagerInterface = boardManager;
        this.gameFlowInterface = gameFlowManager;
    }

    @Override
    public void run() {
        // Crear flujos de entrada/salida
        mediator.notify("createIOStreams()");

        // Intercambiar información de jugadores
        playerManagerInterface.exchangePlayers();

        // Configurar el tablero
        boardManagerInterface.exchangeSetup();

        // Iniciar el flujo del juego
        gameFlowInterface.playGame();
    }
}