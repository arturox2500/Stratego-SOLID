package edu.asu.stratego.client.gui.board.BoardSquareEventPane;

import edu.asu.stratego.client.clientGameManager.gameLogic.GameLogic;
import edu.asu.stratego.client.gui.board.BoardSquarePane;
import edu.asu.stratego.client.gui.board.setup.SetupPieces;
import edu.asu.stratego.client.media.ImageConstants;
import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.game.GameStatus;
import edu.asu.stratego.common.game.MoveStatus;
import edu.asu.stratego.common.game.Piece;
import edu.asu.stratego.common.game.PieceColor;
import edu.asu.stratego.common.game.board.ClientSquare;
import edu.asu.stratego.common.game.boardPieces.PieceTypeInterface;
import edu.asu.stratego.common.util.HashTables;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
/**
 * This event is fired when the player clicks on the event square.
 */
public class SelectSquare implements SelectSquareInterface {
    private BoardSquareEventPaneLogicInterface logic;

    public SelectSquare(BoardSquareEventPaneLogicInterface logic) {
        this.logic = logic;
    }

    @Override
    public void handle(MouseEvent e) {
        // Square position.
        ImageView source = (ImageView) e.getSource();

        int row = GridPane.getRowIndex((Node) source);
        int col = GridPane.getColumnIndex((Node) source);

        // The square and the piece at this position.
        BoardSquarePane squarePane = Game.getBoard()
                .getSquare(row, col)
                .getPiecePane();

        ClientSquare square = Game.getBoard().getSquare(row, col);
        Piece squarePiece = square.getPiece();

        // Player color.
        PieceColor playerColor = Game.getPlayer().getColor();

        /* Game Setup Handler */
        if (Game.getStatus() == GameStatus.SETTING_UP && logic.isHoverValid(row, col)) {

            // Get the selected piece (piece type and count) from the SetupPanel.
            PieceTypeInterface selectedPiece = SetupPieces.getSelectedPieceType();
            int selectedPieceCount = 0;
            if (selectedPiece != null)
                selectedPieceCount = SetupPieces.getPieceCount(selectedPiece);

            // If the square contains a piece...
            if (squarePiece != null) {

                // Remove the existing piece if it is the same piece on board as the
                // selected piece (in SetupPanel) or if no piece is selected (in SetupPanel).
                if (squarePiece.getPieceType() == selectedPiece || selectedPiece == null) {
                    if (squarePiece.getPieceType() != null)
                        SetupPieces.incrementPieceCount(squarePiece.getPieceType());
                    squarePane.setPiece(null);
                    square.setPiece(null);
                }

                // Replace the existing piece with the selected piece (in SetupPanel).
                else if (squarePiece.getPieceType() != selectedPiece && selectedPieceCount > 0) {
                    SetupPieces.decrementPieceCount(selectedPiece);
                    SetupPieces.incrementPieceCount(squarePiece.getPieceType());
                    square.setPiece(new Piece(selectedPiece, playerColor, false));
                    squarePane.setPiece(HashTables.PIECE_MAP.get(square.getPiece().getPieceSpriteKey()));
                }
            }

            // Otherwise, if the square does not contain a piece...
            else {
                // Place a new piece on the square.
                if (selectedPiece != null && selectedPieceCount > 0) {
                    square.setPiece(new Piece(selectedPiece, playerColor, false));
                    squarePane.setPiece(HashTables.PIECE_MAP.get(square.getPiece().getPieceSpriteKey()));
                    SetupPieces.decrementPieceCount(selectedPiece);
                }
            }
        } else if (Game.getStatus() == GameStatus.IN_PROGRESS && Game.getTurn() == playerColor) {
            // If it is the first piece being selected to move (start)
            if (Game.getMoveStatus() == MoveStatus.NONE_SELECTED && logic.isHoverValid(row, col)) {
                Game.getMove().setStart(row, col);

                // Backup opacity check to fix rare race condition
                Game.getBoard().getSquare(row, col).getPiecePane().getPiece().setOpacity(1.0);

                // Update the movestatus to reflect a start has been selected
                Game.setMoveStatus(MoveStatus.START_SELECTED);

                // Calculate and display the valid moves upon selecting the piece
                BoardSquareEventPane.setValidMoves(BoardSquareEventPane.computeValidMoves(row, col));
                BoardSquareEventPane.displayValidMoves(row, col);
            }
            // If a start piece has already been selected, but user is changing start piece
            else if (Game.getMoveStatus() == MoveStatus.START_SELECTED
                    && !BoardSquareEventPaneLogic.isNullPiece(row, col)) {
                Piece highlightPiece = Game.getBoard().getSquare(row, col).getPiece();
                if (highlightPiece.getPieceColor() == playerColor) {
                    Game.getMove().setStart(row, col);

                    // Backup opacity check to fix rare race condition
                    Game.getBoard().getSquare(row, col).getPiecePane().getPiece().setOpacity(1.0);

                    // Calculate and display the valid moves upon selecting the piece
                    BoardSquareEventPane.setValidMoves(BoardSquareEventPane.computeValidMoves(row, col));
                    BoardSquareEventPane.displayValidMoves(row, col);
                }
            }
            if (Game.getMoveStatus() == MoveStatus.START_SELECTED && logic.isValidMove(row, col, BoardSquareEventPane.getValidMoves())) {
                // Remove the hover off all pieces
                for (int rowClear = 0; rowClear < 10; ++rowClear) {
                    for (int colClear = 0; colClear < 10; ++colClear) {
                        Game.getBoard().getSquare(rowClear, colClear).getEventPane().getHover()
                                .setImage(ImageConstants.HIGHLIGHT_NONE);
                        Game.getBoard().getSquare(rowClear, colClear).getEventPane().getHover().setOpacity(1.0);
                    }
                }

                // Set the end location and color in the move
                Game.getMove().setEnd(row, col);
                Game.getMove().setMoveColor(Game.getPlayer().getColor());

                // Change the movestatus to reflect that the end point has been selected
                Game.setMoveStatus(MoveStatus.END_SELECTED);

                synchronized (GameLogic.getSendMove()) {
                    GameLogic.getSendMove().notify();
                }
            }
        }
    }
}
