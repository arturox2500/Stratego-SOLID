package edu.asu.stratego.server.serverGameManager.moveValidator;

import java.util.ArrayList;

import edu.asu.stratego.common.game.PieceColor;

import java.awt.Point;

public interface MoveValidatorInterface {
    ArrayList<Point> computeValidMoves(int row, int col, PieceColor inColor);
}
