package edu.asu.stratego.client.gui.board.setup.SetupTimer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.util.Duration;

public class StartTimer implements StartTimeInterface {
    private int START_TIME;
    private IntegerProperty secondsLeft;
    private Timeline timeline;
    private TimerFinishedInterface timeFinishedHandler;

    public StartTimer(Timeline timeline) {
        this.timeline = timeline;
    }

    @Override
    public void setStartTime(int startTime) {
        this.START_TIME = startTime;
    }

    @Override
    public void setsecondsLeft(IntegerProperty secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    @Override
    public void setTimeFinishedHandler(TimerFinishedInterface timeFinishedHandler) {
        this.timeFinishedHandler = timeFinishedHandler;
    }

    @Override
    public void run() {
        secondsLeft.set(START_TIME);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(START_TIME + 1),
                new KeyValue(secondsLeft, 0)));
        timeline.playFromStart();
        timeline.setOnFinished(timeFinishedHandler);
    }
}