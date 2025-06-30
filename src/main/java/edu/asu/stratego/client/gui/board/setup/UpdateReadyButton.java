package edu.asu.stratego.client.gui.board.setup;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Worker task that waits for a Setup Piece to be incremented or
 * decremented. If all of the pieces have been placed, this task removes
 * the instructions from the panel and adds the ready button to the panel.
 * If the ready button is
 */
public class UpdateReadyButton implements UpdateReadyButtonInterface {
    private Object updateReadyStatus;
    private StackPane instructionPane;
    private Label instructions;
    private ImageView readyButton;

    public UpdateReadyButton(Object updateReadyStatus, StackPane instructionPane,
                             Label instructions, ImageView readyButton) {
        this.updateReadyStatus = updateReadyStatus;
        this.instructionPane = instructionPane;
        this.instructions = instructions;
        this.readyButton = readyButton;
    }

    @Override
    public void run() {
        // instructionPane should update only when the state is changed.
        boolean readyState = false;

        synchronized (updateReadyStatus) {
            while (true) {
                try {
                    // Wait for piece type to increment / decrement.
                    updateReadyStatus.wait();

                    // Remove instructions, add ready button.
                    if (SetupPieces.getAllPiecesPlaced() && !readyState) {
                        Platform.runLater(() -> {
                            instructionPane.getChildren().remove(instructions);
                            instructionPane.getChildren().add(readyButton);
                        });

                        readyState = true;
                    }

                    // Remove ready button, add instructions.
                    else if (!SetupPieces.getAllPiecesPlaced() && readyState) {
                        Platform.runLater(() -> {
                            instructionPane.getChildren().remove(readyButton);
                            instructionPane.getChildren().add(instructions);
                        });

                        readyState = false;
                    }
                } catch (InterruptedException e) {
                    // TODO Handle this exception somehow...
                }
            }
        }
    }
}