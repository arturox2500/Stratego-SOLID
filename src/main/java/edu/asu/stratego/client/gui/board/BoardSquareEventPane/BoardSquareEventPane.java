package edu.asu.stratego.client.gui.board.BoardSquareEventPane;

import java.awt.Point;
import java.util.ArrayList;

import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import edu.asu.stratego.client.media.ImageConstants;
import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.game.boardPieces.Bomb;
import edu.asu.stratego.common.game.boardPieces.Flag;
import edu.asu.stratego.common.game.boardPieces.PieceTypeInterface;
import edu.asu.stratego.common.game.boardPieces.Scout;

/**
 * A single square within the BoardEventPane.
 */
public class BoardSquareEventPane extends GridPane {
    
    private static ArrayList<Point> validMoves;
    private ImageView hover;
    
    /**
     * Creates a new instance of BoardSquareEventPane.
     */
    public BoardSquareEventPane(OnHoverInterface onHover, OffHaverInterface offHover, SelectSquareInterface selectSquare) {
        this.hover = new ImageView(ImageConstants.HIGHLIGHT_NONE);

        // Event handlers for the square.
        hover.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, onHover);
        hover.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, offHover);
        hover.addEventHandler(MouseEvent.MOUSE_CLICKED, selectSquare);
        
        this.getChildren().add(hover);
    }       
    
    public static void displayValidMoves(int pieceRow, int pieceCol) {
    	// Iterate through and unhighlight/unglow all squares/pieces
        for (int row = 0; row < 10; ++row) {
            for (int col = 0; col < 10; ++col) {
                Game.getBoard().getSquare(row, col).getEventPane().getHover().setImage(ImageConstants.HIGHLIGHT_NONE);
                Game.getBoard().getSquare(row, col).getEventPane().getHover().setOpacity(1.0);
                Game.getBoard().getSquare(row, col).getPiecePane().getPiece().setEffect(new Glow(0.0));                      
            }
        }

        // Glow and set a white highlight around the selected piece
        Game.getBoard().getSquare(pieceRow,pieceCol).getPiecePane().getPiece().setEffect(new Glow(0.75));                      
        Game.getBoard().getSquare(pieceRow,pieceCol).getEventPane().getHover().setImage(ImageConstants.HIGHLIGHT_WHITE);

        // Iterate through all valid moves and highlight respective squares
        for (Point validMove : validMoves) {
            Game.getBoard().getSquare((int) validMove.getX(), (int) validMove.getY()).getEventPane().getHover().setImage(ImageConstants.HIGHLIGHT_VALID);
            Game.getBoard().getSquare((int) validMove.getX(), (int) validMove.getY()).getEventPane().getHover().setOpacity(0.5);
        }
    }
    
    public static ArrayList<Point> computeValidMoves(int row, int col) {    	
    	// Set the max distance of a valid move to 1
    	int max = 1;
    	
    	// Set the max distance of a valid move to the board width if the piece is a scout
    	PieceTypeInterface pieceType = Game.getBoard().getSquare(row, col).getPiece().getPieceType();
    	if(pieceType instanceof Scout)
    		max = 8;
    	
    	ArrayList<Point> validMoves = new ArrayList<Point>();
    	
    	// Iterate through each direction and add valid moves based on if:
    	// 1) The square is in bounds (inside the board)
    	// 2) If the square is not a lake
    	// 3) If the square has no piece on it OR there is a piece, but it is an opponent piece
    	
    	if(!(pieceType instanceof Bomb) && !(pieceType instanceof Flag)) {
	    	// Negative Row (UP)
	    	for(int i = -1; i >= -max; --i) {
	    		if(BoardSquareEventPaneLogic.isInBounds(row+i,col) && (!BoardSquareEventPaneLogic.isLake(row+i, col) || (!BoardSquareEventPaneLogic.isNullPiece(row+i, col) && !BoardSquareEventPaneLogic.isOpponentPiece(row+i, col)))) {
	    			if(BoardSquareEventPaneLogic.isNullPiece(row+i, col) || BoardSquareEventPaneLogic.isOpponentPiece(row+i, col)) {
	    				validMoves.add(new Point(row+i, col));
	    				
	    				if(!BoardSquareEventPaneLogic.isNullPiece(row+i, col) && BoardSquareEventPaneLogic.isOpponentPiece(row+i, col))
	    					break;
	    			}
	    			else
	    			    break;
	    		}
	    		else
	    			break;
	    	}
	    	// Positive Col (RIGHT)
	    	for(int i = 1; i <= max; ++i) {
	    		if(BoardSquareEventPaneLogic.isInBounds(row,col+i) && (!BoardSquareEventPaneLogic.isLake(row, col+i) || (!BoardSquareEventPaneLogic.isNullPiece(row, col+i) && !BoardSquareEventPaneLogic.isOpponentPiece(row, col+i)))) {
	    			if(BoardSquareEventPaneLogic.isNullPiece(row, col+i) || BoardSquareEventPaneLogic.isOpponentPiece(row, col+i)) {
	    				validMoves.add(new Point(row, col+i));
	    				
	    				if(!BoardSquareEventPaneLogic.isNullPiece(row, col+i) && BoardSquareEventPaneLogic.isOpponentPiece(row, col+i))
	    					break;
	    			}
	    			else
                        break;
	    		}
	    		else
	    			break;
	    	}
	    	// Positive Row (DOWN)
	    	for(int i = 1; i <= max; ++i) {
	    		if(BoardSquareEventPaneLogic.isInBounds(row+i,col) && (!BoardSquareEventPaneLogic.isLake(row+i, col) || (!BoardSquareEventPaneLogic.isNullPiece(row+i, col) && !BoardSquareEventPaneLogic.isOpponentPiece(row+i, col)))) {
	    			if(BoardSquareEventPaneLogic.isNullPiece(row+i, col) || BoardSquareEventPaneLogic.isOpponentPiece(row+i, col)) {
	    				validMoves.add(new Point(row+i, col));
	    				
	    				if(!BoardSquareEventPaneLogic.isNullPiece(row+i, col) && BoardSquareEventPaneLogic.isOpponentPiece(row+i, col))
	    					break;
	    			}
	    			else
                        break;
	    		}
	    		else
	    			break;
	    	}
	    	// Negative Col (LEFT)
	    	for(int i = -1; i >= -max; --i) {
	    		if(BoardSquareEventPaneLogic.isInBounds(row,col+i) && (!BoardSquareEventPaneLogic.isLake(row, col+i) || (!BoardSquareEventPaneLogic.isNullPiece(row, col+i) && !BoardSquareEventPaneLogic.isOpponentPiece(row, col+i)))) {
	    			if(BoardSquareEventPaneLogic.isNullPiece(row, col+i) || BoardSquareEventPaneLogic.isOpponentPiece(row, col+i)) {
	    				validMoves.add(new Point(row, col+i));
	    				
	    				if(!BoardSquareEventPaneLogic.isNullPiece(row, col+i) && BoardSquareEventPaneLogic.isOpponentPiece(row, col+i))
	    					break;
	    			}
	    			else
                        break;
	    		}
	    		else
	    			break;
	    	}
    	}
    	
    	return validMoves;
    }
    
    /**
     * @return the ImageView object that displays the hover image.
     */
    public ImageView getHover() {
        return hover;
    }

    public static void setValidMoves(ArrayList<Point> validMoves) {
        BoardSquareEventPane.validMoves = validMoves;
    }

    public static ArrayList<Point> getValidMoves() {
        return validMoves;
    }
}