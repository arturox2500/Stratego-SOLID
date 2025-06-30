package edu.asu.stratego.client.gui.board.BoardSquareEventPane;

import java.util.ArrayList;

import java.awt.Point;
import javafx.scene.image.ImageView;

public interface BoardSquareEventPaneLogicInterface {

    public void invalidMove(ImageView inImage);
    public void validMove(ImageView inImage);
    public void checkMove(int row, int col, ImageView hover);
    public boolean isValidMove(int row, int col, ArrayList<Point> validMoves);
    public boolean isHoverValid(int row, int col);
}
