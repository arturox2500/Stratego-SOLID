package edu.asu.stratego.server.serverGameManager.gameFlow;

import edu.asu.stratego.common.game.Move;
import edu.asu.stratego.common.game.Piece;
import edu.asu.stratego.server.serverGameManager.boardManager.BoardManagerInterface;
import java.awt.Point;

public class WinStrategy implements BattleResolutionStrategy {
    @Override
    public void resolve(Move moveToPlayerOne, Move moveToPlayerTwo, Move move, Piece attackingPiece, Piece defendingPiece, BoardManagerInterface board) {
        board.getBoard().getSquare(move.getEnd().x, move.getEnd().y).setPiece(attackingPiece);
        board.getBoard().getSquare(move.getStart().x, move.getStart().y).setPiece(null);

        moveToPlayerOne.setStart(new Point(9 - move.getStart().x, 9 - move.getStart().y));
        moveToPlayerOne.setEnd(new Point(9 - move.getEnd().x, 9 - move.getEnd().y));
        moveToPlayerOne.setMoveColor(move.getMoveColor());
        moveToPlayerOne.setStartPiece(null);
        moveToPlayerOne.setEndPiece(attackingPiece);
        moveToPlayerOne.setAttackWin(true);
        moveToPlayerOne.setDefendWin(false);

        moveToPlayerTwo.setStart(new Point(move.getStart().x, move.getStart().y));
        moveToPlayerTwo.setEnd(new Point(move.getEnd().x, move.getEnd().y));
        moveToPlayerTwo.setMoveColor(move.getMoveColor());
        moveToPlayerTwo.setStartPiece(null);
        moveToPlayerTwo.setEndPiece(attackingPiece);
        moveToPlayerTwo.setAttackWin(true);
        moveToPlayerTwo.setDefendWin(false);
    }
}
