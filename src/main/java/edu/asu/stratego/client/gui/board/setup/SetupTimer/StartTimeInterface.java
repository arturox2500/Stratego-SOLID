package edu.asu.stratego.client.gui.board.setup.SetupTimer;

import javafx.beans.property.IntegerProperty;

public interface StartTimeInterface extends Runnable {
    @Override
    public void run();
    public void setStartTime(int startTime);
    public void setsecondsLeft(IntegerProperty secondsLeft);
    public void setTimeFinishedHandler(TimerFinishedInterface timeFinishedHandler);
}
