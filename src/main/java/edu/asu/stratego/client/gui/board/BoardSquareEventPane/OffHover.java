package edu.asu.stratego.client.gui.board.BoardSquareEventPane;

import edu.asu.stratego.client.media.ImageConstants;
import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.game.GameStatus;
import edu.asu.stratego.common.game.MoveStatus;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class OffHover implements OffHaverInterface {
    @Override
    public void handle(MouseEvent e) {
        ImageView hover = (ImageView) e.getSource();            
        // Change the behavior of the off hover based on the game/move status
        if(Game.getStatus() == GameStatus.SETTING_UP)
            hover.setImage(ImageConstants.HIGHLIGHT_NONE);
        else if(Game.getStatus() == GameStatus.WAITING_OPP) 
            hover.setImage(ImageConstants.HIGHLIGHT_NONE);
        else if(Game.getStatus() == GameStatus.IN_PROGRESS) { 
            if(Game.getMoveStatus() == MoveStatus.OPP_TURN) 
                hover.setImage(ImageConstants.HIGHLIGHT_NONE);
            else if(Game.getMoveStatus() == MoveStatus.NONE_SELECTED)
                hover.setImage(ImageConstants.HIGHLIGHT_NONE);
            else if(Game.getMoveStatus() == MoveStatus.START_SELECTED) {
                // Moved elsewhere: Function to only allow highlighting of squares piece can move to 
            }
        }
    };
}
