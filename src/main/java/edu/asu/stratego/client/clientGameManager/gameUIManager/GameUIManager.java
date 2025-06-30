package edu.asu.stratego.client.clientGameManager.gameUIManager;

import javafx.application.Platform;
import edu.asu.stratego.client.clientGameManager.serverConnection.ServerConnectionInterface;
import edu.asu.stratego.client.gui.ViewInterface;
import edu.asu.stratego.client.gui.Scenes.ConnectionScene.ConnectToServerInterface;
import edu.asu.stratego.client.gui.Scenes.ConnectionScene.ConnectionScene;
import edu.asu.stratego.client.gui.board.setup.SelectPieceInterface;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.SetupTimerInterface;
import edu.asu.stratego.client.media.ImageConstants;
import edu.asu.stratego.common.chat.ChatController;
import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.game.GameStatus;
import edu.asu.stratego.common.game.PieceColor;
import edu.asu.stratego.common.game.Player;
import edu.asu.stratego.common.game.SetupBoard;
import edu.asu.stratego.common.game.board.ClientSquare;

public class GameUIManager implements GameUIManagerInterface {
    private final ViewInterface stage;
    private static Object setupPieces = new Object();
    private static ConnectToServerInterface connectToServerInterface;

    public GameUIManager(ViewInterface stage) {
        this.stage = stage;
    }

    /**
     * @return Object used for communication between the Setup Board GUI and
     *         the ClientGameManager to indicate when the player has finished
     *         setting
     *         up their pieces.
     */
    public static Object getSetupPieces() {
        return setupPieces;
    }

    @Override
    public void connectToServer() {
        try {
            ConnectToServerInterface connectToServer = ConnectionScene.getConnectToServer();
            Thread serverConnect = new Thread(connectToServer);
            serverConnect.setDaemon(true);
            serverConnect.start();
            serverConnect.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void waitForOpponent(ServerConnectionInterface serverConnectionInterface) {
        serverConnectionInterface.connectToServer();
        Platform.runLater(() -> stage.switchScene("WaitingScene"));

        try {
            serverConnectionInterface.sendObject(Game.getPlayer());
            Game.setOpponent((Player) serverConnectionInterface.receiveObject());

            if (Game.getOpponent().getColor() == PieceColor.RED) {
                Game.getPlayer().setColor(PieceColor.BLUE);
            } else {
                Game.getPlayer().setColor(PieceColor.RED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setConnectToServerInterface(ConnectToServerInterface connectToServerInterface) {
        GameUIManager.connectToServerInterface = connectToServerInterface;
    }

    @Override
    public void setupBoard(ServerConnectionInterface serverConnectionInterface, SetupTimerInterface setupTimer,
            SelectPieceInterface selectPiece) {

        ChatController chatController = new ChatController(Game.getPlayer().getNickname(), _ -> {
            javafx.application.Platform.runLater(() -> {
            });
        });

        Platform.runLater(() -> stage.switchScene("BoardScene", setupTimer, selectPiece, Game.getPlayer().getNickname(),
                chatController, connectToServerInterface));

        synchronized (getSetupPieces()) {
            try {
                getSetupPieces().wait();
                Game.setStatus(GameStatus.WAITING_OPP);

                SetupBoard initial = new SetupBoard();
                initial.getPiecePositions();
                serverConnectionInterface.sendObject(initial);

                SetupBoard opponentInitial = (SetupBoard) serverConnectionInterface.receiveObject();

                Platform.runLater(() -> {
                    for (int row = 0; row < 4; ++row) {
                        for (int col = 0; col < 10; ++col) {
                            ClientSquare square = Game.getBoard().getSquare(row, col);
                            square.setPiece(opponentInitial.getPiece(row, col));

                            if (Game.getPlayer().getColor() == PieceColor.RED) {
                                square.getPiecePane().setPiece(ImageConstants.BLUE_BACK);
                            } else {
                                square.getPiecePane().setPiece(ImageConstants.RED_BACK);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showFinalScene(String winner, String loser, ServerConnectionInterface serverConnection) {
        Platform.runLater(() -> {
            stage.switchScene("FinalScene", winner, loser, serverConnection);
        });
    }
}