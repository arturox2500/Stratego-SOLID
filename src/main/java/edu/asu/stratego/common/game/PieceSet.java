package edu.asu.stratego.common.game;

import java.util.ArrayList;

import edu.asu.stratego.common.game.boardPieces.PieceManager;
import edu.asu.stratego.common.game.boardPieces.PieceTypeInterface;

/**
 * 
 */
public class PieceSet {
    private ArrayList<Piece> pieces = new ArrayList<Piece>();
    
    public PieceSet(PieceColor color) {
        PieceColor player = Game.getPlayer().getColor();
        boolean isOpponentPiece = (player != color);
        
        for (PieceTypeInterface type : PieceManager.getAllPieces()) {
            for (int i = 0; i < type.getCount(); ++i) {
                pieces.add(new Piece(type, color, isOpponentPiece));
            }
        }
    }
}