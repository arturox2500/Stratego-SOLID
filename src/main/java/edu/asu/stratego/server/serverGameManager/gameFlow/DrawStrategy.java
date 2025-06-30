package edu.asu.stratego.server.serverGameManager.gameFlow;

import edu.asu.stratego.common.game.Move;
import edu.asu.stratego.common.game.Piece;
import edu.asu.stratego.server.serverGameManager.boardManager.BoardManagerInterface;
import java.awt.Point;

public class DrawStrategy implements BattleResolutionStrategy {
    @Override
    public void resolve(Move moveToPlayerOne, Move moveToPlayerTwo, Move move, Piece attackingPiece, Piece defendingPiece, BoardManagerInterface board) {
        board.getBoard().getSquare(move.getStart().x, move.getStart().y).setPiece(null);
        board.getBoard().getSquare(move.getEnd().x, move.getEnd().y).setPiece(null);

        // Rotate the move 180 degrees before sending.
        moveToPlayerOne.setStart(new Point(9 - move.getStart().x, 9 - move.getStart().y));
        moveToPlayerOne.setEnd(new Point(9 - move.getEnd().x, 9 - move.getEnd().y));
        moveToPlayerOne.setMoveColor(move.getMoveColor());
        moveToPlayerOne.setStartPiece(null);
        moveToPlayerOne.setEndPiece(null);
        moveToPlayerOne.setAttackWin(false);
        moveToPlayerOne.setDefendWin(false);
        
        moveToPlayerTwo.setStart(new Point(move.getStart().x, move.getStart().y));
        moveToPlayerTwo.setEnd(new Point(move.getEnd().x, move.getEnd().y));
        moveToPlayerTwo.setMoveColor(move.getMoveColor());
        moveToPlayerTwo.setStartPiece(null);
        moveToPlayerTwo.setEndPiece(null);
        moveToPlayerTwo.setAttackWin(false);
        moveToPlayerTwo.setDefendWin(false);
    }
}