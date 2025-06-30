package edu.asu.stratego.client.gui.board.BoardSquareEventPane;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import javafx.scene.image.ImageView;
import edu.asu.stratego.client.gui.board.BoardSquarePane;
import edu.asu.stratego.client.gui.board.setup.SetupPanel;
import edu.asu.stratego.client.gui.board.setup.SetupPieces;
import edu.asu.stratego.client.media.ImageConstants;
import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.game.GameStatus;
import edu.asu.stratego.common.game.MoveStatus;
import edu.asu.stratego.common.game.Piece;
import edu.asu.stratego.common.game.PieceColor;
import edu.asu.stratego.common.game.board.ClientSquare;
import edu.asu.stratego.common.game.boardPieces.PieceManager;
import edu.asu.stratego.common.game.boardPieces.PieceTypeInterface;
import edu.asu.stratego.common.util.HashTables;

public class BoardSquareEventPaneLogic implements BoardSquareEventPaneLogicInterface {
    public BoardSquareEventPaneLogic() {
    }

    @Override
    public void invalidMove(ImageView inImage) {
    	inImage.setImage(ImageConstants.HIGHLIGHT_INVALID);
    }

    // Set the image to a green highlight indicating a valid move
    @Override
    public void validMove(ImageView inImage) {
    	inImage.setImage(ImageConstants.HIGHLIGHT_VALID);
    }

    // Check if the move is valid and set the hover accordingly
    @Override
    public void checkMove(int row, int col, ImageView hover) {
        if (isHoverValid(row, col))
            validMove(hover);
        else
            invalidMove(hover);
    }

    @Override
    public boolean isValidMove(int row, int col, ArrayList<Point> validMoves) {
    	// Iterate through validMoves arraylist and check if a square is a valid move (after computing valid moves)
    	if(validMoves != null && validMoves.size() > 0) {
    		for(int i = 0; i < validMoves.size(); i++) {
    			if(row == validMoves.get(i).getX() && col == validMoves.get(i).getY()) 
    				return true;
    		}
    	}
    	return false;
    }

    protected static boolean isLake(int row, int col) {
    	if (col == 2 || col == 3 || col == 6 || col == 7) {
            if (row == 4 || row == 5)
                return true;
        }
    	return false;
    }
    
    // Returns false if the given square is outside of the board
    protected static boolean isInBounds(int row, int col) {
    	if(row < 0 || row > 9)
    		return false;
    	if(col < 0 || col > 9)
    		return false;
    	
    	return true;
    }
    
    // Returns true if the piece is the opponent (from the client's perspective)
    protected static boolean isOpponentPiece(int row, int col) {
    	return Game.getBoard().getSquare(row, col).getPiece().getPieceColor() != Game.getPlayer().getColor();
    }
    
    // Returns true if the piece is null
    protected static boolean isNullPiece(int row, int col) {
    	return Game.getBoard().getSquare(row, col).getPiece() == null;
    }

    /**
     * During the Setup phase of the game, this method randomly places the 
     * pieces that have not yet been placed when the Setup Timer hits 0.
     */
    public static void randomSetup() {
        PieceColor playerColor = Game.getPlayer().getColor();
        
        // Iterate through each square
        for (int col = 0; col < 10; ++col) {
            for (int row = 6; row < 10; ++row) {
                BoardSquarePane squarePane = Game.getBoard().getSquare(row, col).getPiecePane();
                ClientSquare square = Game.getBoard().getSquare(row, col);
                Piece squarePiece = square.getPiece();
               
                // Create an arraylist of all the available values
                List<PieceTypeInterface> availTypes = 
                        PieceManager.getAllPieces();

                // If the square is null (will not overwrite existing pieces)
                if(squarePiece == null) {
                    PieceTypeInterface pieceType = null;
                     
                    // While the pieceType that is going to be placed is null, loop finding a random one
                    // checking that its count is > 0
                    while(pieceType == null) {
                        int randInt = (int) (Math.random() * availTypes.size());
                        if(SetupPieces.getPieceCount(availTypes.get(randInt)) > 0)
                            pieceType = availTypes.get(randInt);
                        // There are no more available for that piecetype, remove it from the array so it won't be randomly generated again
                        else
                            availTypes.remove(randInt);
                    }

                    // Set the square to the piecetype once a suitable piecetype has been found
                    square.setPiece(new Piece(pieceType, playerColor, false));
                    squarePane.setPiece(HashTables.PIECE_MAP.get(square.getPiece().getPieceSpriteKey()));

                    // And lower the availability count of that piece
                    SetupPieces.decrementPieceCount(pieceType);
                }
            }
        }
        
        // Trigger finishSetup so the game will begin
        SetupPanel.finishSetup();
    }

    /**
     * Indicates whether or not a square is a valid square to click.
     * 
     * @param row row index of the square
     * @param col column index of the square
     * @return true if the square is valid, false otherwise
     */
    @Override
    public boolean isHoverValid(int row, int col) {
    	PieceColor playerColor = Game.getPlayer().getColor();
    	
        /* Initially assumes that the square is valid. */
        
        // Lakes are always invalid.
        if (BoardSquareEventPaneLogic.isLake(row, col))
                return false;
        
        // The game is setting up and the square is outside of the setup area.
        if (Game.getStatus() == GameStatus.SETTING_UP && row <= 5)
            return false;
        
        // The player has finished setting up and is waiting for the opponent.
        else if (Game.getStatus() == GameStatus.WAITING_OPP)
            return false;
        
        else if (Game.getStatus() == GameStatus.IN_PROGRESS) {
        	if(Game.getMoveStatus() == MoveStatus.OPP_TURN)
        		return false;
        		
        	if(Game.getMoveStatus() == MoveStatus.NONE_SELECTED) {
        		if(Game.getBoard().getSquare(row, col).getPiece() != null) {
        			Piece highlightPiece = Game.getBoard().getSquare(row, col).getPiece();
        			
        			if(highlightPiece.getPieceColor() != playerColor)
        				return false;
        		} else 
        			return false;
        	}
        }
        
        return true;
    }
}
