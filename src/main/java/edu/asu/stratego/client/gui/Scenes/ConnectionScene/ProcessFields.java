package edu.asu.stratego.client.gui.Scenes.ConnectionScene;

import edu.asu.stratego.client.clientGameManager.gameUIManager.GameUIManager;
import edu.asu.stratego.common.game.Game;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

/**
 * Event handler task for submitFields button events. Notifies the
 * connectToServer thread that connection information has been received
 * from the user.
 *
 * <p>
 * The method call to wait() will cause the event to hang until it is woken
 * up by another thread signaling that a connection attempt has been made.
 * Until the thread running this task is notified, the form fields will
 * be disabled preventing the user from firing another event.
 * </p>
 *
 * @see edu.asu.stratego.client.gui.Scenes.ConnectionScene.ConnectToServer
 */
public class ProcessFields implements ProcessFieldsInterface {
    private String nickname;
    private String serverIP;
    private Button submitFields;
    private TextField nicknameField;
    private TextField serverIPField;
    private Label statusLabel;

    public ProcessFields(Button submitFields, TextField nicknameField, TextField serverIPField, Label statusLabel) {
        this.submitFields = submitFields;
        this.nicknameField = nicknameField;
        this.serverIPField = serverIPField;
        this.statusLabel = statusLabel;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            this.statusLabel.setText("Connecting to the server...");
        });

        this.nickname = this.nicknameField.getText();
        this.serverIP = this.serverIPField.getText();

        // Default values.
        if (this.nickname.equals(""))
            this.nickname = "Player";
        if (serverIP.equals(""))
            this.serverIP = "localhost";

        Game.getPlayer().setNickname(this.nickname);

        this.nicknameField.setEditable(false);
        this.serverIPField.setEditable(false);
        this.submitFields.setDisable(true);

        ConnectionScene.getConnectToServer().setServerIP(this.serverIP);
        GameUIManager.setConnectToServerInterface(ConnectionScene.getConnectToServer());
        synchronized (ConnectionScene.playerLogin) {
            try {
                ConnectionScene.playerLogin.notify(); // Signal submitFields button event.
                ConnectionScene.playerLogin.wait(); // Wait for connection attempt.
            } catch (InterruptedException e) {
                // TODO Handle this exception somehow...
                e.printStackTrace();
            }
        }

        this.nicknameField.setEditable(true);
        this.serverIPField.setEditable(true);
        this.submitFields.setDisable(false);
    }
}
