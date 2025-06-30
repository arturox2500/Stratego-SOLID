package edu.asu.stratego.client.gui.board.BoardSquareEventPane;

import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.game.GameStatus;
import edu.asu.stratego.common.game.MoveStatus;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * This event is triggered when the player's cursor is hovering over the
 * BoardSquareEventPane. It changes the hover image to indicate to the user
 * whether or not a square is valid.
 */
public class OnHover implements OnHoverInterface {
    private BoardSquareEventPaneLogicInterface logic;

    public OnHover(BoardSquareEventPaneLogicInterface logic) {
        this.logic = logic;
    }

    @Override
    public void handle(MouseEvent e) {
        ImageView hover = (ImageView) e.getSource();
        int row = GridPane.getRowIndex(hover);
        int col = GridPane.getColumnIndex(hover);

        // Setting up
        if (Game.getStatus() == GameStatus.SETTING_UP) {
            logic.checkMove(row, col, hover);
        }
        // Waiting for opponent
        else if (Game.getStatus() == GameStatus.WAITING_OPP) {
            logic.invalidMove(hover);
        }
        // In progress
        else if (Game.getStatus() == GameStatus.IN_PROGRESS) {
            if (Game.getMoveStatus() == MoveStatus.OPP_TURN)
                logic.invalidMove(hover);
            else if (Game.getMoveStatus() == MoveStatus.NONE_SELECTED)
                logic.checkMove(row, col, hover);
            else if (Game.getMoveStatus() == MoveStatus.START_SELECTED) {
                // <moved to be handled elsewhere>
            }
        }
    };
}
