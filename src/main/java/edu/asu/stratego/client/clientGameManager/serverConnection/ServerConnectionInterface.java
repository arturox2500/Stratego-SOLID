package edu.asu.stratego.client.clientGameManager.serverConnection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface ServerConnectionInterface {
    public void connectToServer();
    public void sendObject(Object obj);
    public Object receiveObject();
    public ObjectOutputStream getOutputStream();
    public ObjectInputStream getInputStream();
    public void close();
}
