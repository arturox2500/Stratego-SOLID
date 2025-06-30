package edu.asu.stratego;

import javafx.application.Application;
import javafx.stage.Stage;
import edu.asu.stratego.client.ClientFactory;
import edu.asu.stratego.client.clientGameManager.clientGameManager.ClientGameInterface;
import edu.asu.stratego.client.gui.ClientStage;
import edu.asu.stratego.client.gui.ViewInterface;
import edu.asu.stratego.common.game.Game;

public class Client extends Application {

    /**
     * The Main entry point for the Client application.
     */
    @Override
    public void start(Stage primaryStage) {
        // (MODEL) Start a new game.
        new Game();

        // (VIEW) Display client GUI on the JavaFX Application thread.
        ViewInterface clientView = new ClientStage();

        // (CONTROLLER) Create the client game using the factory
        ClientFactory clientFactory = new ClientFactory();
        ClientGameInterface clientGameManager = clientFactory.createClientGame(clientView);

        // Run the client game in a separate thread
        Thread manager = new Thread(clientGameManager);
        manager.setDaemon(true);
        manager.start();
    }

    /**
     * The main method is only needed for the IDE with limited JavaFX support.
     * Not needed for running from the command line.
     *
     * @param args the arguments entered from command line
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}