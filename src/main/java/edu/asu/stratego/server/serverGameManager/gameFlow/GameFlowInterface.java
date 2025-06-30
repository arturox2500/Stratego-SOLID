package edu.asu.stratego.server.serverGameManager.gameFlow;

import edu.asu.stratego.common.game.GameStatus;

public interface GameFlowInterface {
    GameStatus checkWinCondition();
    void playGame();
}
