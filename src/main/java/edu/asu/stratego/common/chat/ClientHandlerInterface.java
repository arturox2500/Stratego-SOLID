package edu.asu.stratego.common.chat;

public interface ClientHandlerInterface extends Runnable {
    @Override
    public void run();
    void sendMessage(String message);

}
