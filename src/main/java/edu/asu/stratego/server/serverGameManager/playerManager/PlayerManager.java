package edu.asu.stratego.server.serverGameManager.playerManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.asu.stratego.common.game.PieceColor;
import edu.asu.stratego.common.game.Player;
import edu.asu.stratego.server.serverGameManager.Component;
import edu.asu.stratego.server.serverGameManager.clientCommunication.ClientCommunicationManager;

public class PlayerManager extends Component implements PlayerManagerInterface {
    private Player playerOne;
    private Player playerTwo;

    public PlayerManager(ClientCommunicationManager communicationManager) {
        this.mediator = communicationManager;
    }

    @Override
    public void exchangePlayers() {
        try {
            playerOne = (Player) ((ObjectInputStream) mediator.notify("getFromPlayerOne()")).readObject();
            playerTwo = (Player) ((ObjectInputStream) mediator.notify("getFromPlayerTwo()")).readObject();

            if (Math.random() < 0.5) {
                playerOne.setColor(PieceColor.RED);
                playerTwo.setColor(PieceColor.BLUE);
            } else {
                playerOne.setColor(PieceColor.BLUE);
                playerTwo.setColor(PieceColor.RED);
            }

            ((ObjectOutputStream) mediator.notify("getToPlayerOne()")).writeObject(playerTwo);
            ((ObjectOutputStream) mediator.notify("getToPlayerTwo()")).writeObject(playerOne);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Player getPlayerOne() {
        if (playerOne == null) {
            throw new IllegalStateException("Player one has not been initialized.");
        }
        return playerOne;
    }

    @Override
    public Player getPlayerTwo() {
        if (playerTwo == null) {
            throw new IllegalStateException("Player two has not been initialized.");
        }
        return playerTwo;
    }
}