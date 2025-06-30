package edu.asu.stratego.client.gui.board.setup.SetupTimer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import edu.asu.stratego.client.clientGameManager.clientGameManager.ClientGameManager;
import edu.asu.stratego.client.gui.ClientStage;

/**
 * A setup timer. This timer counts down from a start time.
 */
public class SetupTimer implements SetupTimerInterface {
    private int START_TIME = 300;
    private Label timerLabel = new Label();
    private IntegerProperty secondsLeft;
    private StartTimeInterface startTimeHandler;
    
    /**
     * Creates a new instance of SetupTimer.
     */
    public SetupTimer(TimerFinishedInterface timeFinishedHandler, StartTimeInterface startTimeHandler) {
        secondsLeft = new SimpleIntegerProperty(START_TIME);
        
        this.startTimeHandler = startTimeHandler;
        this.startTimeHandler.setStartTime(START_TIME);
        this.startTimeHandler.setsecondsLeft(secondsLeft);
        this.startTimeHandler.setTimeFinishedHandler(timeFinishedHandler);

        final double UNIT = ClientStage.getUnit();
        
        timerLabel.textProperty().bind(secondsLeft.asString());
        timerLabel.setFont(Font.font("Century Gothic", UNIT / 3));
        timerLabel.setTextFill(new Color(0.9, 0.5, 0.0, 1.0));
        timerLabel.setAlignment(Pos.TOP_LEFT);
    }
    
    /**
     * Creates a new thread to start the timer task.
     */
    @Override
    public void startTimer() {
        if (ClientGameManager.RANDOM_SETUP == true) {
            START_TIME = 0; 
            secondsLeft = new SimpleIntegerProperty(START_TIME);
            this.startTimeHandler.setStartTime(START_TIME);
            this.startTimeHandler.setsecondsLeft(secondsLeft);
            timerLabel.textProperty().bind(secondsLeft.asString());
        } 

        Thread startTimer = new Thread(this.startTimeHandler);
        startTimer.setDaemon(true);
        startTimer.start();
    }
    
    /**
     * @return JavaFX label that displays how many seconds are left in the 
     * timer.
     */
    @Override
    public Label getLabel() {
        return timerLabel;
    }
}