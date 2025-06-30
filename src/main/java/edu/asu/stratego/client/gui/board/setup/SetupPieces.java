package edu.asu.stratego.client.gui.board.setup;

import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import edu.asu.stratego.client.gui.ClientStage;
import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.game.boardPieces.PieceManager;
import edu.asu.stratego.common.game.boardPieces.PieceTypeInterface;
import edu.asu.stratego.common.util.HashTables;
import edu.asu.stratego.common.util.MutableBoolean;

/**
 * Pieces in the SetupPanel that the player can select when setting up the game.
 * 
 * @see edu.asu.stratego.client.gui.board.setup.SetupPanel
 */
public class SetupPieces {
    private static HashMap<PieceTypeInterface, MutableBoolean> pieceSelected = new HashMap<PieceTypeInterface, MutableBoolean>(
            12);

    private static HashMap<PieceTypeInterface, Integer> availability = new HashMap<PieceTypeInterface, Integer>(12);

    private static HashMap<PieceTypeInterface, ImageView> pieceImages = new HashMap<PieceTypeInterface, ImageView>(12);

    private static HashMap<PieceTypeInterface, Label> pieceCount = new HashMap<PieceTypeInterface, Label>(12);

    private static PieceTypeInterface selectedPieceType;

    private static ColorAdjust zeroPieces = new ColorAdjust();
    private static boolean allPiecesPlaced;

    public static HashMap<PieceTypeInterface, MutableBoolean> getPieceSelected() {
        return pieceSelected;
    }

    public static HashMap<PieceTypeInterface, Integer> getAvailability() {
        return availability;
    }

    public static HashMap<PieceTypeInterface, Label> getPieceCount() {
        return pieceCount;
    }

    public static HashMap<PieceTypeInterface, ImageView> getPieceImagesMap() {
        return pieceImages;
    }

    /**
     * Creates a new instance of SetupPieces.
     */
    public SetupPieces(SelectPieceInterface selectPiece) {
        final double UNIT = ClientStage.getUnit();
        zeroPieces.setSaturation(-1.0);
        selectedPieceType = null;

        // Get the player color.
        String playerColor = Game.getPlayer().getColor().toString();

        // ImageConstants suffixes.
        String[] pieceSuffix = new String[] { "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "BOMB", "SPY", "FLAG" };

        // Number of pieces of each type a player has at the start of the game.
        int[] pieceTypeCount = new int[] { 8, 5, 4, 4, 4, 3, 2, 1, 1, 6, 1, 1 };

        for (int i = 0; i < 12; ++i) {
            // Enumeration values of PieceType.
            PieceTypeInterface pieceType = PieceManager.getAllPieces().get(i);

            // Map the piece type to the number of pieces a player can have of that type
            // at the start of the game.
            availability.put(pieceType, pieceTypeCount[i]);

            // Map the piece type to a label that displays the number of pieces that have
            // not yet been set on the board.
            pieceCount.put(pieceType, new Label(" x" + availability.get(pieceType)));
            pieceCount.get(pieceType).setFont(Font.font("Century Gothic", UNIT * 0.4));
            pieceCount.get(pieceType).setTextFill(new Color(1.0, 1.0, 1.0, 1.0));

            // Map the piece type to its corresponding image.
            pieceImages.put(pieceType, new ImageView(HashTables.PIECE_MAP.get(playerColor + "_" + pieceSuffix[i])));
            pieceImages.get(pieceType).setFitHeight(UNIT * 0.8);
            pieceImages.get(pieceType).setFitWidth(UNIT * 0.8);
            GridPane.setColumnIndex(pieceImages.get(pieceType), i);

            // Register event handlers.
            pieceImages.get(pieceType).addEventHandler(MouseEvent.MOUSE_PRESSED, selectPiece);

            // Map the piece type to a boolean value that denotes whether or not the
            // SetupPiece is selected. Initially, none of the pieces are selected.
            pieceSelected.put(pieceType, new MutableBoolean(false));
        }
    }

    /**
     * @return the type of the selected piece
     */
    public static PieceTypeInterface getSelectedPieceType() {
        return selectedPieceType;
    }

    public static void setSelectedPieceType(PieceTypeInterface selectedPieceType) {
        SetupPieces.selectedPieceType = selectedPieceType;
    }

    /**
     * @param type PieceType
     * @return the number of pieces of the PieceType have not been set on the
     *         board yet
     */
    public static int getPieceCount(PieceTypeInterface type) {
        return availability.get(type);
    }

    /**
     * Increments the piece type count by 1 and updates the piece type label.
     * Signals SetupPanel to update the ready button if all of the pieces are
     * placed.
     * 
     * @param type PieceType to increment
     */
    public static void incrementPieceCount(PieceTypeInterface type) {
        availability.put(type, availability.get(type) + 1);
        pieceCount.get(type).setText(" x" + availability.get(type));

        if (availability.get(type) == 1)
            pieceImages.get(type).setEffect(new Glow(0.0));
        allPiecesPlaced = false;

        Object updateReadyStatus = SetupPanel.getUpdateReadyStatus();
        synchronized (updateReadyStatus) {
            updateReadyStatus.notify();
        }
    }

    /**
     * Decrements the piece type count by 1 and updates the piece type label.
     * Runs a check to see if all the pieces have been placed. Signals
     * SetupPanel to update the ready button if all of the pieces are placed.
     * 
     * @param type PieceType to decrement
     */
    public static void decrementPieceCount(PieceTypeInterface type) {
        availability.put(type, availability.get(type) - 1);
        pieceCount.get(type).setText(" x" + availability.get(type));

        if (availability.get(type) == 0) {
            pieceImages.get(type).setEffect(zeroPieces);
            pieceSelected.get(type).setFalse();
            selectedPieceType = null;
        }

        allPiecesPlaced = true;
        for (PieceTypeInterface pieceType : PieceManager.getAllPieces()) {
            if (availability.get(pieceType) > 0)
                allPiecesPlaced = false;
        }

        Object updateReadyStatus = SetupPanel.getUpdateReadyStatus();
        synchronized (updateReadyStatus) {
            updateReadyStatus.notify();
        }
    }

    /**
     * @return true if all the pieces have been placed, false otherwise
     */
    public static boolean getAllPiecesPlaced() {
        return allPiecesPlaced;
    }

    /**
     * @return an array of ImageView objects that display images corresponding
     *         to the piece type.
     */
    public ImageView[] getPieceImages() {
        ImageView[] images = new ImageView[12];

        for (int i = 0; i < 12; ++i) {
            PieceTypeInterface pieceType = PieceManager.getAllPieces().get(i);
            images[i] = pieceImages.get(pieceType);
        }

        return images;
    }

    /**
     * @return an array of JavaFX labels that display the number of pieces of
     *         each piece type that still need to be placed.
     */
    public Label[] getPieceCountLabels() {
        Label[] pieceCountLabels = new Label[12];

        for (int i = 0; i < 12; ++i) {
            PieceTypeInterface pieceType = PieceManager.getAllPieces().get(i);
            pieceCountLabels[i] = pieceCount.get(pieceType);
        }

        return pieceCountLabels;
    }
}