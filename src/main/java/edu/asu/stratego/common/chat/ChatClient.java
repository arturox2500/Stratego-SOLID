package edu.asu.stratego.common.chat;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<String> messageHandler;
    private String playerName;
    private String sessionId;

    public ChatClient(Consumer<String> messageHandler, String playerName) {
        this.playerName = playerName;
        this.messageHandler = messageHandler;
    }

    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Envía el nombre del jugador como primer mensaje
            out.println(playerName);

            // Leer el sessionId como primer mensaje del servidor
            String firstMsg = in.readLine();
            if (firstMsg != null && firstMsg.startsWith("SESSIONID:")) {
                sessionId = firstMsg.substring("SESSIONID:".length());
                System.out.println("Session ID received: " + sessionId);
            }

            new Thread(() -> {
                String msg;
                try {
                    while ((msg = in.readLine()) != null) {
                        messageHandler.accept(msg);
                    }
                } catch (IOException e) {
                    // Solo muestra el stacktrace si no es una desconexión esperada
                    if (e.getMessage() != null && e.getMessage().contains("Connection reset")) {
                        System.out.println("The other player has disconnected from the chat.");
                    } else {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        if (out != null && !msg.isBlank() && sessionId != null) {
            out.println(sessionId + ":" + msg);
        }
    }
}
