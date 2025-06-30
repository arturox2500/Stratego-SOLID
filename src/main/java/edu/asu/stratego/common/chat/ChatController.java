package edu.asu.stratego.common.chat;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class ChatController {

    private final ChatClient client;
    private TextArea chatArea;
    private TextField inputField;

    public ChatController(String playerName, Consumer<String> onMessageReceived) {
        this.client = new ChatClient(message -> Platform.runLater(() -> {
            if (chatArea != null) {
                chatArea.appendText("Opponent: " + message + "\n");
            }
            onMessageReceived.accept(message);
        }), playerName);
    }

    public void connect(String host, int port) {
        client.connect(host, port);
    }

    public void attachUI(TextArea chatArea, TextField inputField) {
        this.chatArea = chatArea;
        this.inputField = inputField;

        // Set event handlers
        inputField.setOnAction(_-> sendMessage());
    }

    public void sendMessage() {
        String text = inputField.getText();
        if (!text.isBlank()) {
            chatArea.appendText("Me: " + text + "\n");
            client.sendMessage(text);
            inputField.clear();
        }
    }
}
