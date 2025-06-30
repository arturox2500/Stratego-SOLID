package edu.asu.stratego.client.gui.Scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import edu.asu.stratego.client.gui.ClientStage;
import edu.asu.stratego.client.gui.Scenes.ConnectionScene.ConnectToServerInterface;
import edu.asu.stratego.client.gui.board.BoardTurnIndicator;
import edu.asu.stratego.client.gui.board.setup.SelectPieceInterface;
import edu.asu.stratego.client.gui.board.setup.SetupPanel;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.SetupTimerInterface;
import edu.asu.stratego.client.media.ImageConstants;
import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.chat.ChatController;

public class BoardScene extends BaseScene {

    private final double UNIT = ClientStage.getUnit();
    private final int SIDE = ClientStage.getSide();

    private static StackPane root = new StackPane();
    private static GridPane setupPanel = new GridPane();
    private ConnectToServerInterface connectToServer;

    private final ChatController chatController;

    public BoardScene(SetupTimerInterface setupTimer, SelectPieceInterface selectPiece, String playerName, ChatController chatController, ConnectToServerInterface connectToServer) {
        this.connectToServer = connectToServer;
        this.chatController = chatController;
        this.buildScene(setupTimer, selectPiece, playerName);
    }

    public static StackPane getRootPane() {
        return root;
    }

    public static GridPane getSetupPanel() {
        return setupPanel;
    }

    @Override
    protected void buildScene(Object... args) {
        new BoardTurnIndicator();
        Rectangle background = BoardTurnIndicator.getTurnIndicator();

        final int size = 10;
        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {
                Game.getBoard().getSquare(row, col).getPiecePane().getPiece().setFitHeight(UNIT);
                Game.getBoard().getSquare(row, col).getPiecePane().getPiece().setFitWidth(UNIT);
                Game.getBoard().getSquare(row, col).getEventPane().getHover().setFitHeight(UNIT);
                Game.getBoard().getSquare(row, col).getEventPane().getHover().setFitWidth(UNIT);
            }
        }

        new SetupPanel((SetupTimerInterface) args[0], (SelectPieceInterface) args[1]);
        setupPanel = SetupPanel.getSetupPanel();
        StackPane.setMargin(setupPanel, new Insets(UNIT, 0, 0, 0));
        StackPane.setAlignment(setupPanel, Pos.TOP_CENTER);

        // Chat UI
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        TextField inputField = new TextField();
        inputField.setPromptText("Type a message...");

        Button sendButton = new Button("Send");

        HBox inputBox = new HBox(10, inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        VBox chatPane = new VBox(10, chatArea, inputBox);
        chatPane.setPrefWidth(SIDE / 3);
        chatPane.setPadding(new Insets(10));
        chatPane.setAlignment(Pos.TOP_CENTER);
        chatPane.setStyle("-fx-background-color: #f0f0f0;");

        // Conectar chat con UI y lógica
        chatController.attachUI(chatArea, inputField);
        chatController.connect(connectToServer.getServerIP(), 12345);

        sendButton.setOnAction(_ -> chatController.sendMessage());

        ImageView border = new ImageView(ImageConstants.BORDER);
        StackPane.setAlignment(border, Pos.TOP_CENTER);
        border.setFitHeight(SIDE);
        border.setFitWidth(SIDE);

        StackPane centerPane = new StackPane(
                background,
                Game.getBoard().getPiecePane(),
                Game.getBoard().getEventPane(),
                border
        );
        centerPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        StackPane.setMargin(Game.getBoard().getEventPane(), new Insets(UNIT, 0, 0, UNIT));
        StackPane.setMargin(Game.getBoard().getPiecePane(), new Insets(UNIT, 0, 0, UNIT));

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(centerPane);
        mainLayout.setRight(chatPane);
        BorderPane.setAlignment(centerPane, Pos.TOP_CENTER);
        BorderPane.setMargin(centerPane, new Insets(0));

        root = new StackPane(mainLayout, setupPanel);

        StackPane.setAlignment(setupPanel, Pos.TOP_LEFT);
        StackPane.setMargin(setupPanel, new Insets(UNIT, 0, 0, UNIT));

        scene = new Scene(root, SIDE + (SIDE / 3), SIDE);
    }
}
