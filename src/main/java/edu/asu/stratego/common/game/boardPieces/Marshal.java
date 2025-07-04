package edu.asu.stratego.common.game.boardPieces;

import java.io.Serializable;

import edu.asu.stratego.common.game.BattleOutcome;
import edu.asu.stratego.common.game.PieceColor;

public class Marshal implements PieceTypeInterface, Serializable {
    @Override public int getValue() { return 10; }
    @Override public int getCount() { return 1; }
    @Override public String getName() { return "Marshal"; }

    @Override
    public BattleOutcome attack(PieceTypeInterface defender) {
        if (defender instanceof Flag || defender instanceof Spy) return BattleOutcome.WIN;
        if (defender instanceof Bomb) return BattleOutcome.LOSE;
        if (defender.getValue() == this.getValue()) return BattleOutcome.DRAW;
        return this.getValue() > defender.getValue() ? BattleOutcome.WIN : BattleOutcome.LOSE;
    }

    @Override
    public String getSpriteKey(PieceColor color, boolean isOpponentPiece) {
        if (isOpponentPiece) return color == PieceColor.RED ? "RED_BACK" : "BLUE_BACK";
        return color == PieceColor.RED ? "RED_10" : "BLUE_10";
    }

}
