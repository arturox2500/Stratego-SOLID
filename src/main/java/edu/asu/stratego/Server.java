package edu.asu.stratego;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import edu.asu.stratego.common.chat.ChatServer;
import edu.asu.stratego.server.GameSessionFactory;
import edu.asu.stratego.server.serverGameManager.serverGameManager.ServerGameInterface;

public class Server {
    public static void main(String[] args) throws IOException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        ServerSocket listener = null;
        int sessionNumber = 1;

        // Start the chat server in a separate thread
        new Thread(() -> {
            ChatServer.start(); 
        }).start();

        try {
            listener = new ServerSocket(4212);
            System.out.println("Server started @ " + hostAddress);
            System.out.println("Waiting for incoming connections...\n");

            GameSessionFactory sessionFactory = new GameSessionFactory();

            while (true) {
                Socket playerOne = listener.accept();
                System.out.println("Session " + sessionNumber + ": Player 1 has joined the session");

                Socket playerTwo = listener.accept();
                System.out.println("Session " + sessionNumber + ": Player 2 has joined the session");

                String sessionName = "Session " + sessionNumber++ + ": ";
                ServerGameInterface serverGame = sessionFactory.createSession(playerOne, playerTwo, sessionName);

                Thread session = new Thread(serverGame);
                session.setDaemon(true);
                session.start();
            }
        } finally {
            if (listener != null) {
                listener.close();
            }
        }
    }
}