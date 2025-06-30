package edu.asu.stratego.common.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static final Set<ClientHandlerInterface> clients = Collections.synchronizedSet(new HashSet<>());
    private static int sessionId = 0;
    private static int numberClients = 0;

    public static void start() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("ChatServer started on port " + PORT);
                while (true) {
                    Socket socket = serverSocket.accept();
                    numberClients++;
                    if (numberClients % 2 != 0){
                        sessionId++;
                    }
                    ClientHandlerInterface handler = new ClientHandler(socket, clients, sessionId);
                    clients.add(handler);
                    new Thread(handler).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
