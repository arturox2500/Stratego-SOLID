package edu.asu.stratego.client.gui.Scenes;

import javafx.scene.Scene;

public abstract class BaseScene {

    protected Scene scene;

    public Scene getScene() {
        return scene;
    }

    protected abstract void buildScene(Object... args);

}
