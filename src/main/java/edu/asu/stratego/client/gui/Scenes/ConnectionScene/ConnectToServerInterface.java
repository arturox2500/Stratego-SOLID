package edu.asu.stratego.client.gui.Scenes.ConnectionScene;

public interface ConnectToServerInterface extends Runnable{
    @Override
    public void run();
    public void setServerIP(String serverIP);
    public String getServerIP();
}
