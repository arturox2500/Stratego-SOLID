package edu.asu.stratego.common.game.board;

import edu.asu.stratego.client.gui.board.BoardSquarePane;
import edu.asu.stratego.client.gui.board.BoardSquareType;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.BoardSquareEventPane;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.OffHaverInterface;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.OnHoverInterface;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.SelectSquareInterface;
import edu.asu.stratego.common.game.Piece;

/**
 * Represents an individual square of a Stratego board.
 */
public class ClientSquare {

    private Piece piece = null;
    private BoardSquarePane piecePane;
    private BoardSquareEventPane eventPane;

    /**
     * Creates a new instance of Square.
     * 
     * @param type the square background image
     */
    public ClientSquare(BoardSquareType type, OnHoverInterface onHover, OffHaverInterface offHover, SelectSquareInterface selectSquare) {
        piecePane = new BoardSquarePane(type);
        eventPane = new BoardSquareEventPane(onHover, offHover, selectSquare);
    }

    /**
     * @return the BoardSquarePane associated with this Square
     */
    public BoardSquarePane getPiecePane() {
        return piecePane;
    }

    /**
     * @return the piece at this square.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * @param piece the piece to set
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * @return the BoardSquareEventPane associated with this Square
     */
    public BoardSquareEventPane getEventPane() {
        return eventPane;
    }
}