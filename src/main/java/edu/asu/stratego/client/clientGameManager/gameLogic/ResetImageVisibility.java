package edu.asu.stratego.client.clientGameManager.gameLogic;

import edu.asu.stratego.common.game.Game;
import javafx.event.ActionEvent;

public class ResetImageVisibility implements ResetImageVisibilityInterface {
    private static Object waitVisible = new Object();

    @Override
        public void handle(ActionEvent event) {
            synchronized (waitVisible) {
                waitVisible.notify();
                Game.getBoard().getSquare(Game.getMove().getStart().x, Game.getMove().getStart().y).getPiecePane()
                        .getPiece().setOpacity(1.0);
                Game.getBoard().getSquare(Game.getMove().getStart().x, Game.getMove().getStart().y).getPiecePane()
                        .getPiece().setRotate(0.0);
                Game.getBoard().getSquare(Game.getMove().getStart().x, Game.getMove().getStart().y).getPiecePane()
                        .setPiece(null);

                Game.getBoard().getSquare(Game.getMove().getEnd().x, Game.getMove().getEnd().y).getPiecePane()
                        .getPiece().setOpacity(1.0);
                Game.getBoard().getSquare(Game.getMove().getEnd().x, Game.getMove().getEnd().y).getPiecePane()
                        .getPiece().setRotate(0.0);
            }
        }
}
