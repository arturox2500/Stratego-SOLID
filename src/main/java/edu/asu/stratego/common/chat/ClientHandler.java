package edu.asu.stratego.common.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

public class ClientHandler implements ClientHandlerInterface {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String playerName;
    private String sessionId;
    private Set<ClientHandlerInterface> clients;

    public ClientHandler(Socket socket, Set<ClientHandlerInterface> clients, int sessionId) {
        this.sessionId = String.valueOf(sessionId);
        this.clients = clients;
        this.socket = socket;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Leer el primer mensaje como nombre del jugador
            playerName = in.readLine();
            System.out.println(playerName + " joined the chat.");

            // Enviar el sessionId al cliente
            out.println("SESSIONID:" + sessionId);

            String message;
            while ((message = in.readLine()) != null) {
                // Espera que el mensaje tenga el formato: sessionId:mensaje
                String[] parts = message.split(":", 2);
                if (parts.length == 2) {
                    String msgSessionId = parts[0];
                    String realMessage = parts[1];
                    if (this.sessionId == null || this.sessionId.equals(msgSessionId)) {
                        synchronized (clients) {
                            for (ClientHandlerInterface client : clients) {
                                if (client != this && client instanceof ClientHandler) {
                                    ClientHandler ch = (ClientHandler) client;
                                    if (ch.getSessionId() == null || ch.getSessionId().equals(msgSessionId)) {
                                        client.sendMessage(realMessage);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            if (playerName != null) {
                System.out.println(playerName + " disconnected from chat.");
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
            clients.remove(this);

            if (clients.isEmpty()) {
                System.out.println(
                        "All the clients have been disconnected. The chat server is waiting for new connections.");
            }
        }
    }
}
