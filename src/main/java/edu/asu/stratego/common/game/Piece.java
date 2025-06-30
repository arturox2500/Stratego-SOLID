package edu.asu.stratego.common.game;

import java.io.Serializable;

import edu.asu.stratego.common.game.boardPieces.PieceTypeInterface;

/**
 * Represents a single game piece.
 */
public class Piece implements Serializable {

    private static final long serialVersionUID = 7193334048398155856L;
    
    private PieceColor color;
    private PieceTypeInterface  type;
    
    private String   spriteKey;
    
    /**
     * Creates a new instance of Piece.
     * 
     * @param type PieceType of the piece
     * @param color color of the piece
     * @param isOpponentPiece whether or not the piece belongs to the opponent
     */
    public Piece(PieceTypeInterface type, PieceColor color, boolean isOpponentPiece) {
        this.color = color;
        this.type  = type;
        this.spriteKey = type.getSpriteKey(color, isOpponentPiece);
    }    
    
    /**
     * @return the piece type of the piece.
     */
    public PieceTypeInterface getPieceType() {
        return type;
    }
    
    public PieceColor getPieceColor() {
        return color;
    }
    
    /**
     * @return the sprite associated with the type of the piece.
     */
    public String getPieceSpriteKey() {
        return spriteKey;
    }
}