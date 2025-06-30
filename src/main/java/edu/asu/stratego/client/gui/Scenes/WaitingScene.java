package edu.asu.stratego.client.gui.Scenes;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Wrapper class for a JavaFX scene. Contains a scene UI to indicate that the 
 * client has successfully connected to a server and is currently waiting for 
 * another opponent to connect to the server. The intended function for this 
 * scene is analogous to a loading screen.
 */
public class WaitingScene extends BaseScene {
    
    private final int WINDOW_WIDTH  = 300;
    private final int WINDOW_HEIGHT = 150;
    
    /**
     * Creates a new instance of WaitingScene.
     */
    public WaitingScene() {
        this.buildScene();
    }

    @Override
    protected void buildScene(Object... args) {
        // Create UI.
        StackPane pane = new StackPane();
        pane.getChildren().add(new Label("Waiting for an opponent..."));
        
        this.scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.scene.setFill(Color.LIGHTGRAY);
    }
}