package edu.asu.stratego.common.game.boardPieces;

import java.io.Serializable;

import edu.asu.stratego.common.game.BattleOutcome;
import edu.asu.stratego.common.game.PieceColor;

public class Flag implements PieceTypeInterface, Serializable {
    @Override public int getValue() { return -1; }
    @Override public int getCount() { return 1; }
    @Override public String getName() { return "Flag"; }

    @Override
    public BattleOutcome attack(PieceTypeInterface defender) {
        return BattleOutcome.LOSE;
    }

    @Override
    public String getSpriteKey(PieceColor color, boolean isOpponentPiece) {
        if (isOpponentPiece) return color == PieceColor.RED ? "RED_BACK" : "BLUE_BACK";
        return color == PieceColor.RED ? "RED_FLAG" : "BLUE_FLAG";
    }

}
