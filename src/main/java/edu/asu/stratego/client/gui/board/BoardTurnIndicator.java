package edu.asu.stratego.client.gui.board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import edu.asu.stratego.client.gui.ClientStage;
import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.game.PieceColor;

/**
 * JavaFX rectangle that is layered behind the semi-transparent board border to 
 * indicate to the players whose turn it is. Depending on which player's turn 
 * it is, the rectangle will be colored either red or blue.
 */
public class BoardTurnIndicator {
    
    private static Color red  = new Color(0.48, 0.13, 0.13, 1.0);
    private static Color blue = new Color(0.22, 0.24, 0.55, 1.0);
    
    private static Object turnIndicatorTrigger = new Object();
    private static Rectangle turnIndicator;
    
    /**
     * Creates a new instance of BoardTurnIndicator.
     */
    public BoardTurnIndicator() {
        final int SIDE = ClientStage.getSide();
        turnIndicator = new Rectangle(0, 0, SIDE, SIDE);
        
        // Set the setup game turn color.
        if (Game.getPlayer().getColor() == PieceColor.RED)
            turnIndicator.setFill(red);
        else
            turnIndicator.setFill(blue);
        
        // Start thread to automatically update turn color.
        Thread updateColor = new Thread(new UpdateColor());
        updateColor.setDaemon(true);
        updateColor.start();
    }
    
    /**
     * @return the turn indicator (JavaFX rectangle)
     */
    public static Rectangle getTurnIndicator() {
        return turnIndicator;
    }
    
    /**
     * @return Object used to communicate between the ClientGameManager and the 
     * BoardTurnIndicator to indicate when the player turn color has changed.
     */
    public static Object getTurnIndicatorTrigger() {
        return turnIndicatorTrigger;
    }
    
    public static Color getRed() {
        return red;
    }

    public static Color getBlue() {
        return blue;
    }
}