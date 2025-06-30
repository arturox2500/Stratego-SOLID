package edu.asu.stratego.server.serverGameManager.boardManager;

import java.awt.Point;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.asu.stratego.common.game.GameStatus;
import edu.asu.stratego.common.game.Piece;
import edu.asu.stratego.common.game.SetupBoard;
import edu.asu.stratego.common.game.board.ServerBoard;
import edu.asu.stratego.common.game.boardPieces.Flag;
import edu.asu.stratego.server.serverGameManager.Component;
import edu.asu.stratego.server.serverGameManager.clientCommunication.ClientCommunicationManager;
import edu.asu.stratego.server.serverGameManager.gameFlow.GameFlowInterface;

public class BoardManager extends Component implements BoardManagerInterface {
    private Point playerOneFlag;
    private Point playerTwoFlag;
    private final ServerBoard board = new ServerBoard();
    private GameFlowInterface gameFlow;

    public BoardManager(ClientCommunicationManager communicationManager) {
        this.mediator = communicationManager;
    }

    @Override
    public void setGameFlow(GameFlowInterface gameFlow) {
        this.gameFlow = gameFlow;
    }

    @Override
    public Point getPlayerOneFlag() {
        return playerOneFlag;
    }

    @Override
    public Point getPlayerTwoFlag() {
        return playerTwoFlag;
    }

    @Override
    public void exchangeSetup() {
        try {
            SetupBoard setupBoardOne = (SetupBoard) ((ObjectInputStream) mediator.notify("getFromPlayerOne()")).readObject();
            SetupBoard setupBoardTwo = (SetupBoard) ((ObjectInputStream) mediator.notify("getFromPlayerTwo()")).readObject();
            
            // Register pieces on the server board.
            for (int row = 0; row < 4; ++row) {
                for (int col = 0; col < 10; ++col) {
                    board.getSquare(row, col).setPiece(setupBoardOne.getPiece(3 - row, 9 - col));
                    board.getSquare(row + 6, col).setPiece(setupBoardTwo.getPiece(row, col));
                    
                    if(setupBoardOne.getPiece(3 - row, 9 - col).getPieceType() instanceof Flag)
                    	playerOneFlag = new Point(row, col);
                    if(setupBoardTwo.getPiece(row, col).getPieceType() instanceof Flag)
                    	playerTwoFlag = new Point(row + 6, col);
                }
            }
            
            // Rotate pieces by 180 degrees.
            for (int row = 0; row < 2; ++row) {
                for (int col = 0; col < 10; ++col) {
                    // Player One
                    Piece temp = setupBoardOne.getPiece(row, col);
                    setupBoardOne.setPiece(setupBoardOne.getPiece(3 - row, 9 - col), row, col);
                    setupBoardOne.setPiece(temp, 3 - row, 9 - col);
                    
                    // Player Two
                    temp = setupBoardTwo.getPiece(row, col);
                    setupBoardTwo.setPiece(setupBoardTwo.getPiece(3 - row, 9 - col), row, col);
                    setupBoardTwo.setPiece(temp, 3 - row, 9 - col);
                }
            }
            
            GameStatus winCondition = gameFlow.checkWinCondition();
            ((ObjectOutputStream) mediator.notify("getToPlayerOne()")).writeObject(setupBoardTwo);
            ((ObjectOutputStream) mediator.notify("getToPlayerTwo()")).writeObject(setupBoardOne);
            
            ((ObjectOutputStream) mediator.notify("getToPlayerOne()")).writeObject(winCondition);
            ((ObjectOutputStream) mediator.notify("getToPlayerTwo()")).writeObject(winCondition);
        }
        catch (ClassNotFoundException | IOException e) {
            // TODO Handle this exception somehow...
            e.printStackTrace();
        }
        
    }

    @Override
    public ServerBoard getBoard() {
        if (board == null) {
            throw new IllegalStateException("Board has not been initialized.");
        }
        return board;
    }
}