package edu.asu.stratego.client.gui.board.setup;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public interface SelectPieceInterface extends EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent e);

}
