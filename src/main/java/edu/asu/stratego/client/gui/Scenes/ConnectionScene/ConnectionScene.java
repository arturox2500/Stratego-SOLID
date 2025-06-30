package edu.asu.stratego.client.gui.Scenes.ConnectionScene;

import edu.asu.stratego.client.clientGameManager.clientGameManager.ClientGameManager;
import edu.asu.stratego.client.gui.Scenes.BaseScene;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Wrapper class for a JavaFX scene. Contains a scene UI and its associated
 * event handlers for retrieving network connection information from the player
 * and connecting to the network.
 */
public class ConnectionScene extends BaseScene {

    static final Object playerLogin = new Object();

    private Button submitFields = new Button("Enter Battlefield");
    private Button randomFields  = new Button("Random Battlefield"); 
    private TextField nicknameField = new TextField();
    private TextField serverIPField = new TextField();
    static Label statusLabel = new Label();

    private final int WIDTH = 350;
    private final int HEIGHT = 200;

    private static ConnectToServerInterface connectToServer;

    /**
     * Creates a new instance of ConnectionScene.
     */
    public ConnectionScene() {
        this.buildScene();
    }

    public static ConnectToServerInterface getConnectToServer() {
        return connectToServer;
    }

    @Override
    protected void buildScene(Object... args) {
        // Create UI.
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Nickname: "), 0, 0);
        gridPane.add(new Label("Server IP: "), 0, 1);
        gridPane.add(nicknameField, 1, 0);
        gridPane.add(serverIPField, 1, 1);
        HBox buttonBox = new HBox(10); 
        buttonBox.getChildren().addAll(submitFields, randomFields);
        buttonBox.setAlignment(Pos.CENTER_RIGHT); 

        gridPane.add(buttonBox, 1, 3);
        BorderPane borderPane = new BorderPane();
        BorderPane.setMargin(statusLabel, new Insets(0, 0, 10, 0));
        BorderPane.setAlignment(statusLabel, Pos.CENTER);
        borderPane.setBottom(statusLabel);
        borderPane.setCenter(gridPane);

        
        GridPane.setHalignment(submitFields, HPos.RIGHT);
        GridPane.setHalignment(randomFields, HPos.RIGHT);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        
        ProcessFieldsInterface processFields = new ProcessFields(submitFields, nicknameField, serverIPField,
                statusLabel);
        submitFields.setOnAction(_ -> Platform.runLater(processFields));

        randomFields.setOnAction(_ -> Platform.runLater(() -> {
            ClientGameManager.RANDOM_SETUP = true;
            processFields.run();
        }));

        scene = new Scene(borderPane, WIDTH, HEIGHT);

        connectToServer = new ConnectToServer(statusLabel);
    }
}
