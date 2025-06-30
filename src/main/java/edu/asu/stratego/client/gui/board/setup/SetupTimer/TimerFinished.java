package edu.asu.stratego.client.gui.board.setup.SetupTimer;

import edu.asu.stratego.client.gui.board.BoardSquareEventPane.BoardSquareEventPaneLogic;
import javafx.event.ActionEvent;

public class TimerFinished implements TimerFinishedInterface {
    @Override
    public void handle(ActionEvent event) {
        BoardSquareEventPaneLogic.randomSetup();
    }
}
