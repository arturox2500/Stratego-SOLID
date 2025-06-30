package edu.asu.stratego.client.gui.Scenes.ConnectionScene;

import java.io.IOException;

import edu.asu.stratego.common.game.ClientSocket;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * A Runnable task for establishing a connection to a Stratego server.
 * The task will continue running until a successful connection has
 * been made. The connection attempt loop is structured like so:
 *
 * <ol>
 * <li>
 * Wait for the player to invoke button event in the ConnectionScene.
 * </li>
 * <li>
 * Attempt to connect to a Stratego server using the information retrieved
 * from the UI and wake up the button event thread.
 * </li>
 * <li>
 * If connection succeeds, signal the isConnected condition to indicate to
 * other threads a successful connection attempt and then terminate the
 * task. Otherwise, output error message to GUI, and go to #1.
 * </li>
 * </ol>
 *
 * @see edu.asu.stratego.client.gui.Scenes.ConnectionScene.ProcessFields
 */
public class ConnectToServer implements ConnectToServerInterface {
    private String serverIP;
    private Label statusLabel;

    public ConnectToServer(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    @Override
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    @Override
    public String getServerIP() {
        return this.serverIP;
    }

    @Override
    public void run() {

        while (ClientSocket.getInstance() == null) {
            synchronized (ConnectionScene.playerLogin) {
                try {
                    // Wait for submitFields button event.
                    ConnectionScene.playerLogin.wait();

                    // Attempt connection to server.
                    ClientSocket.connect(this.serverIP, 4212);
                } catch (IOException | InterruptedException e) {
                    Platform.runLater(() -> {
                        this.statusLabel.setText("Cannot connect to the Server");
                    });
                } finally {
                    // Wake up button event thread.
                    ConnectionScene.playerLogin.notify();
                }
            }
        }
    }

}
