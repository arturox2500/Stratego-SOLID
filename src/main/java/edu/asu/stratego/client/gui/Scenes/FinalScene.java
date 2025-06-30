package edu.asu.stratego.client.gui.Scenes;

import edu.asu.stratego.client.clientGameManager.serverConnection.ServerConnectionInterface;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Wrapper class for a JavaFX scene. Contains a scene UI to indicate that the client has finish the game, 
 * show the winner and the loser and a botton to close the window and the process.
 */
public class FinalScene extends BaseScene {

    private final int WINDOW_WIDTH = 400;
    private final int WINDOW_HEIGHT = 200;

    /**
     * Creates a new instance of WaitingScene.
     */
    public FinalScene(Object... args) {
        this.buildScene(args);
    }

    @Override
    protected void buildScene(Object... args) {
        // Verifica que los argumentos incluyan el ganador y el perdedor
        if (args.length < 2 || !(args[0] instanceof String) || !(args[1] instanceof String)) {
            throw new IllegalArgumentException("Se requieren los nombres del ganador y perdedor como argumentos.");
        }

        String winner = (String) args[0];
        String loser = (String) args[1];

        ServerConnectionInterface serverConnection = (ServerConnectionInterface) args[2];

        // Crear el diseño principal
        StackPane pane = new StackPane();

        // Etiqueta para mostrar el ganador
        Label winnerLabel = new Label("Winner: " + winner);
        winnerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: green;");

        // Etiqueta para mostrar el perdedor
        Label loserLabel = new Label("Loser: " + loser);
        loserLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");

        // Botón para cerrar la ventana
        Button closeButton = new Button("Close window");
        closeButton.setOnAction(_ -> {
            // Limpia recursos antes de salir
            cleanupResources(serverConnection);
            javafx.application.Platform.exit();
            System.exit(0); // Asegúrate de que todos los procesos se terminen
        });
        // Contenedor vertical para organizar los elementos
        VBox layout = new VBox(20, winnerLabel, loserLabel, closeButton);
        layout.setAlignment(Pos.CENTER);

        // Agregar el diseño al panel principal
        pane.getChildren().add(layout);

        // Crear la escena
        this.scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.scene.setFill(Color.LIGHTGRAY);
    }

    private void cleanupResources(ServerConnectionInterface serverConnection) {
        try {
            // Cierra la conexión con el servidor
            if (serverConnection != null) {
                serverConnection.close();
            }

            // Detén cualquier hilo activo
            if (Thread.currentThread().isAlive()) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
