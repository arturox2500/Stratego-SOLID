package edu.asu.stratego.common.game.boardPieces;

import edu.asu.stratego.common.game.BattleOutcome;
import edu.asu.stratego.common.game.PieceColor;

public interface PieceTypeInterface {
    int getValue();
    int getCount();
    BattleOutcome attack(PieceTypeInterface defender);
    String getSpriteKey(PieceColor color, boolean isOpponentPiece);
    String getName();
}
