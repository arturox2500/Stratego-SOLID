package edu.asu.stratego.client.gui.board.BoardSquareEventPane;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public interface SelectSquareInterface extends EventHandler<MouseEvent> {
    @Override
    void handle(MouseEvent e);

}
