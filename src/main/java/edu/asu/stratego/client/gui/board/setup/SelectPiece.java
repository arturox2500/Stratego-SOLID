package edu.asu.stratego.client.gui.board.setup;

import edu.asu.stratego.common.game.boardPieces.PieceManager;
import edu.asu.stratego.common.game.boardPieces.PieceTypeInterface;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SelectPiece implements SelectPieceInterface {
    @Override
    public void handle(MouseEvent e) {
        ImageView pieceImage = (ImageView) e.getSource();

        // Iterate through the PieceType enumerated values.
        for (int i = 0; i < 12; ++i) {
            PieceTypeInterface pieceType = PieceManager.getAllPieces().get(i);

            // If the piece type does not match the piece type selected...
            if (SetupPieces.getPieceImagesMap().get(pieceType) != pieceImage) {
                // Un-select the piece type.
                if (SetupPieces.getAvailability().get(pieceType) != 0)
                    SetupPieces.getPieceImagesMap().get(pieceType).setEffect(new Glow(0.0));
                SetupPieces.getPieceSelected().get(pieceType).setFalse();
            }

            // Otherwise...
            else {
                // Select the piece type if not already selected.
                if (!SetupPieces.getPieceSelected().get(pieceType).getValue() &&
                        SetupPieces.getAvailability().get(pieceType) != 0) {
                    SetupPieces.setSelectedPieceType(pieceType);
                    SetupPieces.getPieceImagesMap().get(pieceType).setEffect(new Glow(1.0));
                    SetupPieces.getPieceSelected().get(pieceType).setTrue();
                }

                // Un-select piece type if already selected.
                else {
                    SetupPieces.setSelectedPieceType(null);
                    if (SetupPieces.getAvailability().get(pieceType) != 0)
                        pieceImage.setEffect(new Glow(0.0));
                    SetupPieces.getPieceSelected().get(pieceType).setFalse();
                }
            }
        }
    }
}
