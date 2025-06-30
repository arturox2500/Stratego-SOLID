package edu.asu.stratego.client.clientGameManager.gameLogic;

import edu.asu.stratego.common.game.Game;
import javafx.event.ActionEvent;

public class ResetSquareImage implements ResetSquareImageInterface {
    private Object waitFade;

    public ResetSquareImage(Object waitFade) {
        this.waitFade = waitFade;
    }
    // Finicky, ill-advised to edit. Resets the opacity, rotation, and piece to null
    // Duplicate "ResetImageVisibility" class was intended to not set piece to null,
    // untested though.
    @Override
    public void handle(ActionEvent event) {
        synchronized (this.waitFade) {
            this.waitFade.notify();
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
