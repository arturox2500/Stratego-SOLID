package edu.asu.stratego.client.clientGameManager.serverConnection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.asu.stratego.common.game.ClientSocket;

public class ServerConnection implements ServerConnectionInterface {
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;

    public ServerConnection() {

    }

    @Override
    public void connectToServer() {
        try {
            this.toServer = new ObjectOutputStream(ClientSocket.getInstance().getOutputStream());
            this.fromServer = new ObjectInputStream(ClientSocket.getInstance().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendObject(Object obj) {
        try {
            toServer.writeObject(obj);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object receiveObject() {
        try {
            return fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ObjectOutputStream getOutputStream() {
        return toServer;
    }

    @Override
    public ObjectInputStream getInputStream() {
        return fromServer;
    }

    @Override
    public void close() {
        try {
            if (toServer != null) {
                toServer.close();
            }
            if (fromServer != null) {
                fromServer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}