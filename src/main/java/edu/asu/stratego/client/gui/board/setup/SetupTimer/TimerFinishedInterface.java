package edu.asu.stratego.client.gui.board.setup.SetupTimer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface TimerFinishedInterface extends EventHandler<ActionEvent>{

    @Override
    public void handle(ActionEvent event);

}
