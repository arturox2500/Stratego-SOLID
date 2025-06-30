package edu.asu.stratego.client.gui.board;

import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.game.PieceColor;
import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.util.Duration;

public class UpdateColor implements UpdateColorInterface {
    @Override
    public void run() {
        synchronized (BoardTurnIndicator.getTurnIndicatorTrigger()) {
            try {
                while (true) {
                    // Wait for player turn color to change.
                    BoardTurnIndicator.getTurnIndicatorTrigger().wait();

                    Platform.runLater(() -> {
                        // Blue -> Red.
                        if (Game.getTurn() == PieceColor.RED &&
                                BoardTurnIndicator.getTurnIndicator().getFill() != BoardTurnIndicator.getRed()) {
                            FillTransition ft = new FillTransition(Duration.millis(2000),
                                    BoardTurnIndicator.getTurnIndicator(), BoardTurnIndicator.getBlue(), BoardTurnIndicator.getRed());
                            ft.play();
                        }

                        // Red -> Blue.
                        else if (Game.getTurn() == PieceColor.BLUE &&
                                BoardTurnIndicator.getTurnIndicator().getFill() != BoardTurnIndicator.getBlue()) {
                            FillTransition ft = new FillTransition(Duration.millis(3000),
                                    BoardTurnIndicator.getTurnIndicator(), BoardTurnIndicator.getRed(), BoardTurnIndicator.getBlue());
                            ft.play();
                        }
                    });
                }
            } catch (InterruptedException e) {
                // TODO Handle this exception somehow...
                e.printStackTrace();
            }
        }
    }
}
