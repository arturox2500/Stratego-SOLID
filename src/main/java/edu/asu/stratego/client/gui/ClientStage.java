package edu.asu.stratego.client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import edu.asu.stratego.client.gui.Scenes.SceneFactory;
import javafx.stage.Stage;

/**
 * The ConnectionStage class, which inherits from the JavaFX Stage class, is a 
 * preset Stage for facilitating easy navigation between scenes in the Client 
 * application.
 */
public class ClientStage extends Stage implements ViewInterface {
    
    private static double UNIT;
    private static int    SIDE;
    
    /**
     * Creates a new instance of ClientStage.
     */
    public ClientStage() {
        // Calculate the BoardScene dimensions from screen resolution.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SIDE = (int) (0.85 * screenSize.getHeight()) / 12 * 12;
        UNIT = SIDE / 12;
        
        this.switchScene("ConnectionScene");
        this.setTitle("ASU Stratego");
        this.setResizable(false);
        this.show();
    }

    @Override
    public void switchScene(String sceneName, Object... args) {
        this.setScene(SceneFactory.createScene(sceneName, args).getScene());
    }

    /**
     * @return the board scene side length (in pixels) divided by 12.
     */
    public static double getUnit() {
        return UNIT;
    }
    
    /**
     * @return the side length of the board scene (in pixels)
     */
    public static int getSide() {
        return SIDE;
    }
}