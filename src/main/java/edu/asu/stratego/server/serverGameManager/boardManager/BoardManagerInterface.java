package edu.asu.stratego.server.serverGameManager.boardManager;

import java.awt.Point;

import edu.asu.stratego.common.game.board.ServerBoard;
import edu.asu.stratego.server.serverGameManager.gameFlow.GameFlowInterface;

public interface BoardManagerInterface {
    void setGameFlow(GameFlowInterface gameFlow);
    void exchangeSetup();
    Point getPlayerOneFlag();
    Point getPlayerTwoFlag(); 
    ServerBoard getBoard();
}
