package edu.asu.stratego.server.serverGameManager.gameFlow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.asu.stratego.common.game.BattleOutcome;
import edu.asu.stratego.common.game.GameStatus;
import edu.asu.stratego.common.game.Move;
import edu.asu.stratego.common.game.Piece;
import edu.asu.stratego.common.game.PieceColor;
import edu.asu.stratego.common.game.boardPieces.Flag;
import edu.asu.stratego.server.serverGameManager.Component;
import edu.asu.stratego.server.serverGameManager.boardManager.BoardManagerInterface;
import edu.asu.stratego.server.serverGameManager.clientCommunication.Mediator;
import edu.asu.stratego.server.serverGameManager.moveValidator.MoveValidatorInterface;
import edu.asu.stratego.server.serverGameManager.playerManager.PlayerManagerInterface;

import java.awt.Point;

public class GameFlowManager extends Component implements GameFlowInterface {
	private final PlayerManagerInterface playerManagerInterface;
	private final BoardManagerInterface boardManagerInterface;
	private PieceColor turn;
	private Move move;
	private final String session;
	private final MoveValidatorInterface moveValidatorInterface;

	public GameFlowManager(Mediator communicationManager, PlayerManagerInterface playerManager,
			BoardManagerInterface boardManager, String session, MoveValidatorInterface moveValidator) {
		this.mediator = communicationManager;
		this.playerManagerInterface = playerManager;
		this.boardManagerInterface = boardManager;
		this.session = session;
		this.moveValidatorInterface = moveValidator;

		if (Math.random() < 0.5)
			this.turn = PieceColor.RED;
		else
			this.turn = PieceColor.BLUE;
	}

	public void playGame() {
		boolean playerOneDisconnected = false;
		boolean playerTwoDisconnected = false;

		while (true) {
			try {
				// Send player turn color to clients.
				((ObjectOutputStream) mediator.notify("getToPlayerOne()")).writeObject(turn);
				((ObjectOutputStream) mediator.notify("getToPlayerTwo()")).writeObject(turn);

				// Get turn from client.
				if (playerManagerInterface.getPlayerOne().getColor() == turn) {
					move = (Move) ((ObjectInputStream) mediator.notify("getFromPlayerOne()")).readObject();
					move.setStart(9 - move.getStart().x, 9 - move.getStart().y);
					move.setEnd(9 - move.getEnd().x, 9 - move.getEnd().y);
				} else {
					move = (Move) ((ObjectInputStream) mediator.notify("getFromPlayerTwo()")).readObject();
				}

				Move moveToPlayerOne = new Move(), moveToPlayerTwo = new Move();

				// Register move on the board.
				// If there is no piece at the end (normal move, no attack)
				if (boardManagerInterface.getBoard().getSquare(move.getEnd().x, move.getEnd().y).getPiece() == null) {
					Piece piece = boardManagerInterface.getBoard().getSquare(move.getStart().x, move.getStart().y)
							.getPiece();

					boardManagerInterface.getBoard().getSquare(move.getStart().x, move.getStart().y).setPiece(null);
					boardManagerInterface.getBoard().getSquare(move.getEnd().x, move.getEnd().y).setPiece(piece);

					// Rotate the move 180 degrees before sending.
					moveToPlayerOne.setStart(new Point(9 - move.getStart().x, 9 - move.getStart().y));
					moveToPlayerOne.setEnd(new Point(9 - move.getEnd().x, 9 - move.getEnd().y));
					moveToPlayerOne.setMoveColor(move.getMoveColor());
					moveToPlayerOne.setStartPiece(null);
					moveToPlayerOne.setEndPiece(piece);

					moveToPlayerTwo.setStart(new Point(move.getStart().x, move.getStart().y));
					moveToPlayerTwo.setEnd(new Point(move.getEnd().x, move.getEnd().y));
					moveToPlayerTwo.setMoveColor(move.getMoveColor());
					moveToPlayerTwo.setStartPiece(null);
					moveToPlayerTwo.setEndPiece(piece);
				} else {
					Piece attackingPiece = boardManagerInterface.getBoard()
							.getSquare(move.getStart().x, move.getStart().y).getPiece();
					Piece defendingPiece = boardManagerInterface.getBoard().getSquare(move.getEnd().x, move.getEnd().y)
							.getPiece();

					BattleOutcome outcome = attackingPiece.getPieceType().attack(defendingPiece.getPieceType());

					// Executes a strategy depending on the battle outcome
					BattleStrategyManager strategyManager = new BattleStrategyManager();
					strategyManager.executeStrategy(outcome, moveToPlayerOne, moveToPlayerTwo, move, attackingPiece,
							defendingPiece, boardManagerInterface);

					moveToPlayerOne.setAttackMove(true);
					moveToPlayerTwo.setAttackMove(true);

				}

				GameStatus winCondition = checkWinCondition();

				((ObjectOutputStream) mediator.notify("getToPlayerOne()")).writeObject(moveToPlayerOne);
				((ObjectOutputStream) mediator.notify("getToPlayerTwo()")).writeObject(moveToPlayerTwo);

				((ObjectOutputStream) mediator.notify("getToPlayerOne()")).writeObject(winCondition);
				((ObjectOutputStream) mediator.notify("getToPlayerTwo()")).writeObject(winCondition);

				// Change turn color.
				if (turn == PieceColor.RED)
					turn = PieceColor.BLUE;
				else
					turn = PieceColor.RED;

				// Check win conditions.
			} catch (IOException | ClassNotFoundException e) {
				// Detecta quién se desconectó según el turno
				if (playerManagerInterface.getPlayerOne().getColor() == turn) {
					playerOneDisconnected = true;
					System.out.println(session + "Player 1 disconnected.");
				} else {
					playerTwoDisconnected = true;
					System.out.println(session + "Player 2 disconnected.");
				}

				// Si ambos están desconectados, salimos del bucle
				if (playerOneDisconnected && playerTwoDisconnected) {
					break;
				}

				// Si solo uno está desconectado, cambiamos el turno y seguimos esperando al
				// otro
				if (turn == PieceColor.RED)
					turn = PieceColor.BLUE;
				else
					turn = PieceColor.RED;
			}
		}
		System.out.println(session + "Both player have been disconnected. Sesion closed");
	}

	public GameStatus checkWinCondition() {
		if (!hasAvailableMoves(PieceColor.RED))
			return GameStatus.RED_NO_MOVES;

		else if (isCaptured(PieceColor.RED))
			return GameStatus.RED_CAPTURED;

		if (!hasAvailableMoves(PieceColor.BLUE))
			return GameStatus.BLUE_NO_MOVES;

		else if (isCaptured(PieceColor.BLUE))
			return GameStatus.BLUE_CAPTURED;

		return GameStatus.IN_PROGRESS;
	}

	private boolean isCaptured(PieceColor inColor) {
		if (playerManagerInterface.getPlayerOne().getColor() == inColor) {
			if (!(boardManagerInterface.getBoard()
					.getSquare(boardManagerInterface.getPlayerOneFlag().x, boardManagerInterface.getPlayerOneFlag().y)
					.getPiece().getPieceType() instanceof Flag))
				return true;
		}
		if (playerManagerInterface.getPlayerTwo().getColor() == inColor) {
			if (!(boardManagerInterface.getBoard()
					.getSquare(boardManagerInterface.getPlayerTwoFlag().x, boardManagerInterface.getPlayerTwoFlag().y)
					.getPiece().getPieceType() instanceof Flag))
				return true;
		}
		return false;
	}

	private boolean hasAvailableMoves(PieceColor inColor) {
		for (int row = 0; row < 10; ++row) {
			for (int col = 0; col < 10; ++col) {
				if (boardManagerInterface.getBoard().getSquare(row, col).getPiece() != null
						&& boardManagerInterface.getBoard().getSquare(row, col).getPiece().getPieceColor() == inColor) {
					if (moveValidatorInterface.computeValidMoves(row, col, inColor).size() > 0) {
						return true;
					}
				}
			}
		}

		return false;
	}
}