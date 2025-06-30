package edu.asu.stratego.server.serverGameManager.gameFlow;

import edu.asu.stratego.common.game.Move;
import edu.asu.stratego.common.game.Piece;
import edu.asu.stratego.server.serverGameManager.boardManager.BoardManagerInterface;

public interface BattleResolutionStrategy {
    void resolve(Move moveToPlayerOne, Move moveToPlayerTwo, Move move, Piece attacker, Piece defender, BoardManagerInterface board);
}
