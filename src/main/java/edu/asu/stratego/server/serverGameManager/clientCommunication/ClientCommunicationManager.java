package edu.asu.stratego.server.serverGameManager.clientCommunication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientCommunicationManager implements Mediator {
    private final Socket socketOne;
    private final Socket socketTwo;
    private ObjectOutputStream toPlayerOne;
    private ObjectOutputStream toPlayerTwo;
    private ObjectInputStream fromPlayerOne;
    private ObjectInputStream fromPlayerTwo;

    public ClientCommunicationManager(Socket socketOne, Socket socketTwo) {
        this.socketOne = socketOne;
        this.socketTwo = socketTwo;
    }

    public void createIOStreams() {
        try {
            toPlayerOne = new ObjectOutputStream(socketOne.getOutputStream());
            fromPlayerOne = new ObjectInputStream(socketOne.getInputStream());
            toPlayerTwo = new ObjectOutputStream(socketTwo.getOutputStream());
            fromPlayerTwo = new ObjectInputStream(socketTwo.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObjectOutputStream getToPlayerOne() {
        return toPlayerOne;
    }

    private ObjectOutputStream getToPlayerTwo() {
        return toPlayerTwo;
    }

    private ObjectInputStream getFromPlayerOne() {
        return fromPlayerOne;
    }

    private ObjectInputStream getFromPlayerTwo() {
        return fromPlayerTwo;
    }

    @Override
    public Object notify(String event) {
        switch (event) {
            case "createIOStreams()":
                createIOStreams();
                return null;
            case "getToPlayerOne()":
                return getToPlayerOne();
            case "getToPlayerTwo()":
                return getToPlayerTwo();
            case "getFromPlayerOne()":
                return getFromPlayerOne();
            case "getFromPlayerTwo()":
                return getFromPlayerTwo();
            default:
                throw new IllegalArgumentException("Unknown event: " + event);
        }
    }
}