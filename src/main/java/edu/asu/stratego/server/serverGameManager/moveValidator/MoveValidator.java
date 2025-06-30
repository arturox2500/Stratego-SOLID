package edu.asu.stratego.server.serverGameManager.moveValidator;

import java.awt.Point;
import java.util.ArrayList;

import edu.asu.stratego.common.game.PieceColor;
import edu.asu.stratego.common.game.board.ServerBoard;
import edu.asu.stratego.common.game.boardPieces.Bomb;
import edu.asu.stratego.common.game.boardPieces.Flag;
import edu.asu.stratego.common.game.boardPieces.PieceTypeInterface;
import edu.asu.stratego.common.game.boardPieces.Scout;

public class MoveValidator implements MoveValidatorInterface {
    private ServerBoard board;

    public MoveValidator(ServerBoard board) {
        this.board = board;
    }

    @Override
    public ArrayList<Point> computeValidMoves(int row, int col, PieceColor inColor) {
        ArrayList<Point> validMoves = new ArrayList<>();

        if (!isInBounds(row, col) || isNullPiece(row, col)) {
            return validMoves;
        }

        PieceTypeInterface pieceType = board.getSquare(row, col).getPiece().getPieceType();

        int max = (pieceType instanceof Scout) ? 8 : 1;

        if (!(pieceType instanceof Bomb) && !(pieceType instanceof Flag)) {
            // Negative Row (UP)
            for (int i = -1; i >= -max; --i) {
                if (isInBounds(row + i, col) && (!isLake(row + i, col)
                        || (!isNullPiece(row + i, col) && !isOpponentPiece(row + i, col, inColor)))) {
                    if (isNullPiece(row + i, col) || isOpponentPiece(row + i, col, inColor)) {
                        validMoves.add(new Point(row + i, col));

                        if (!isNullPiece(row + i, col) && isOpponentPiece(row + i, col, inColor))
                            break;
                    } else
                        break;
                } else
                    break;
            }
            // Positive Col (RIGHT)
            for (int i = 1; i <= max; ++i) {
                if (isInBounds(row, col + i) && (!isLake(row, col + i)
                        || (!isNullPiece(row, col + i) && !isOpponentPiece(row, col + i, inColor)))) {
                    if (isNullPiece(row, col + i) || isOpponentPiece(row, col + i, inColor)) {
                        validMoves.add(new Point(row, col + i));

                        if (!isNullPiece(row, col + i) && isOpponentPiece(row, col + i, inColor))
                            break;
                    } else
                        break;
                } else
                    break;
            }
            // Positive Row (DOWN)
            for (int i = 1; i <= max; ++i) {
                if (isInBounds(row + i, col) && (!isLake(row + i, col)
                        || (!isNullPiece(row + i, col) && !isOpponentPiece(row + i, col, inColor)))) {
                    if (isNullPiece(row + i, col) || isOpponentPiece(row + i, col, inColor)) {
                        validMoves.add(new Point(row + i, col));

                        if (!isNullPiece(row + i, col) && isOpponentPiece(row + i, col, inColor))
                            break;
                    } else
                        break;
                } else
                    break;
            }
            // Negative Col (LEFT)
            for (int i = -1; i >= -max; --i) {
                if (isInBounds(row, col + i) && (!isLake(row, col + i)
                        || (!isNullPiece(row, col + i) && !isOpponentPiece(row, col + i, inColor)))) {
                    if (isNullPiece(row, col + i) || isOpponentPiece(row, col + i, inColor)) {
                        validMoves.add(new Point(row, col + i));

                        if (!isNullPiece(row, col + i) && isOpponentPiece(row, col + i, inColor))
                            break;
                    } else
                        break;
                } else
                    break;
            }
        }

        return validMoves;
    }

    private static boolean isLake(int row, int col) {
        if (col == 2 || col == 3 || col == 6 || col == 7) {
            if (row == 4 || row == 5)
                return true;
        }
        return false;
    }

    private static boolean isInBounds(int row, int col) {
        if (row < 0 || row > 9)
            return false;
        if (col < 0 || col > 9)
            return false;

        return true;
    }
    
    private boolean isOpponentPiece(int row, int col, PieceColor inColor) {
        return board.getSquare(row, col).getPiece().getPieceColor() != inColor;
    }

    private boolean isNullPiece(int row, int col) {
        return board.getSquare(row, col).getPiece() == null;
    }
}