package edu.asu.stratego.common.game.board;

import edu.asu.stratego.client.gui.board.BoardEventPane;
import edu.asu.stratego.client.gui.board.BoardPane;
import edu.asu.stratego.client.gui.board.BoardSquareType;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.BoardSquareEventPaneLogic;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.BoardSquareEventPaneLogicInterface;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.OffHaverInterface;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.OffHover;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.OnHover;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.OnHoverInterface;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.SelectSquare;
import edu.asu.stratego.client.gui.board.BoardSquareEventPane.SelectSquareInterface;

/**
 * Representation of a Stratego board.
 */
public class ClientBoard {

    private final BoardPane piecePane;
    private final BoardEventPane eventPane;
    private final int size = 10;
    private ClientSquare[][] squares;

    /**
     * Creates a new instance of Board.
     */
    public ClientBoard() {
        // Initialize the board GUI.
        squares = new ClientSquare[size][size];
        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {
                if ((row + col) % 2 == 0) {
                    BoardSquareEventPaneLogicInterface logic = new BoardSquareEventPaneLogic();
                    OnHoverInterface onHover = new OnHover(logic);
                    OffHaverInterface offHover = new OffHover();
                    SelectSquareInterface selectSquare = new SelectSquare(logic);
                    squares[row][col] = new ClientSquare(BoardSquareType.DARK, onHover, offHover, selectSquare);
                } else {
                    BoardSquareEventPaneLogicInterface logic = new BoardSquareEventPaneLogic();
                    OnHoverInterface onHover = new OnHover(logic);
                    OffHaverInterface offHover = new OffHover();
                    SelectSquareInterface selectSquare = new SelectSquare(logic);
                    squares[row][col] = new ClientSquare(BoardSquareType.LIGHT, onHover, offHover, selectSquare);
                }
            }
        }

        // Initialize board layers.
        piecePane = new BoardPane(this);
        eventPane = new BoardEventPane(this);
    }

    /**
     * Returns the board square located at (row, col).
     * 
     * @param row board square row
     * @param col board square column
     * @return the square located at (row, col)
     */
    public ClientSquare getSquare(int row, int col) {
        return squares[row][col];
    }

    /**
     * @return the BoardPane.
     */
    public BoardPane getPiecePane() {
        return piecePane;
    }

    /**
     * @return the BoardEventPane.
     */
    public BoardEventPane getEventPane() {
        return eventPane;
    }
}