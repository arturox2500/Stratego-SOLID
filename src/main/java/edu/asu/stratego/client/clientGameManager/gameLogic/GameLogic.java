package edu.asu.stratego.client.clientGameManager.gameLogic;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import edu.asu.stratego.client.clientGameManager.gameUIManager.GameUIManagerInterface;
import edu.asu.stratego.client.clientGameManager.serverConnection.ServerConnectionInterface;
import edu.asu.stratego.client.gui.Scenes.BoardScene;
import edu.asu.stratego.client.gui.board.BoardTurnIndicator;
import edu.asu.stratego.client.media.ImageConstants;
import edu.asu.stratego.common.game.Game;
import edu.asu.stratego.common.game.GameStatus;
import edu.asu.stratego.common.game.Move;
import edu.asu.stratego.common.game.MoveStatus;
import edu.asu.stratego.common.game.Piece;
import edu.asu.stratego.common.game.PieceColor;
import edu.asu.stratego.common.game.board.ClientSquare;
import edu.asu.stratego.common.game.boardPieces.Scout;
import edu.asu.stratego.common.util.HashTables;

public class GameLogic implements GameLogicInterface {
    private final ServerConnectionInterface serverConnectionInterface;
    private final GameUIManagerInterface gameUIManager;

    private static Object sendMove = new Object();
    private static Object receiveMove = new Object();
    private static Object waitFade = new Object();

    public GameLogic(ServerConnectionInterface serverConnection, GameUIManagerInterface gameUIManagerInterface) {
        this.serverConnectionInterface = serverConnection;
        this.gameUIManager = gameUIManagerInterface;
    }

    public static Object getReceiveMove() {
        return receiveMove;
    }

    public static Object getSendMove() {
        return sendMove;
    }

    @Override
    public void playGame() {
        removeSetupPanel();
        Game.setStatus((GameStatus) serverConnectionInterface.receiveObject());

        while (Game.getStatus() == GameStatus.IN_PROGRESS) {
            try {
                handleTurn();
                sendMoveIfPlayerTurn();
                receiveAndProcessMove();
                updateBoardsAndAnimations();
                waitFadeAnimation();
                Game.setStatus((GameStatus) serverConnectionInterface.receiveObject());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        revealAll();

        // Llamar a showFinalScene cuando el juego termine
        if (Game.getStatus() != GameStatus.IN_PROGRESS) {
            String winner = determineWinner(Game.getStatus());
            String loser = determineLoser(Game.getStatus());

            // Crear un retraso de 10 segundos
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(10));
            delay.setOnFinished(_ -> gameUIManager.showFinalScene(winner, loser, serverConnectionInterface));
            delay.play();
        }
    }

    private String determineWinner(GameStatus status) {
        if (status == GameStatus.RED_CAPTURED || status == GameStatus.RED_NO_MOVES) {
            // Si el jugador rojo pierde, el azul gana
            return Game.getOpponent().getColor() == PieceColor.BLUE ? Game.getOpponent().getNickname()
                    : Game.getPlayer().getNickname();
        } else if (status == GameStatus.BLUE_CAPTURED || status == GameStatus.BLUE_NO_MOVES) {
            // Si el jugador azul pierde, el rojo gana
            return Game.getOpponent().getColor() == PieceColor.RED ? Game.getOpponent().getNickname()
                    : Game.getPlayer().getNickname();
        }
        return "Unknown";
    }

    private String determineLoser(GameStatus status) {
        if (status == GameStatus.RED_CAPTURED || status == GameStatus.RED_NO_MOVES) {
            // Si el jugador rojo pierde
            return Game.getOpponent().getColor() == PieceColor.RED ? Game.getOpponent().getNickname()
                    : Game.getPlayer().getNickname();
        } else if (status == GameStatus.BLUE_CAPTURED || status == GameStatus.BLUE_NO_MOVES) {
            // Si el jugador azul pierde
            return Game.getOpponent().getColor() == PieceColor.BLUE ? Game.getOpponent().getNickname()
                    : Game.getPlayer().getNickname();
        }
        return "Unknown";
    }

    private void removeSetupPanel() {
        Platform.runLater(() -> {
            BoardScene.getRootPane().getChildren().remove(BoardScene.getSetupPanel());
        });
    }

    private void handleTurn() throws InterruptedException {
        Game.setTurn((PieceColor) serverConnectionInterface.receiveObject());

        if (Game.getPlayer().getColor() == Game.getTurn())
            Game.setMoveStatus(MoveStatus.NONE_SELECTED);
        else
            Game.setMoveStatus(MoveStatus.OPP_TURN);

        synchronized (BoardTurnIndicator.getTurnIndicatorTrigger()) {
            BoardTurnIndicator.getTurnIndicatorTrigger().notify();
        }
    }

    private void sendMoveIfPlayerTurn() throws InterruptedException {
        if (Game.getPlayer().getColor() == Game.getTurn() && Game.getMoveStatus() != MoveStatus.SERVER_VALIDATION) {
            synchronized (sendMove) {
                sendMove.wait();
                serverConnectionInterface.sendObject(Game.getMove());
                Game.setMoveStatus(MoveStatus.SERVER_VALIDATION);
            }
        }
    }

    private void receiveAndProcessMove() throws InterruptedException {
        Game.setMove((Move) serverConnectionInterface.receiveObject());
        Piece startPiece = Game.getMove().getStartPiece();
        Piece endPiece = Game.getMove().getEndPiece();

        if (Game.getMove().isAttackMove()) {
            handleAttackMove();
        }

        updateSoftwareBoard(startPiece, endPiece);
    }

    private void handleAttackMove() throws InterruptedException {
        Piece attackingPiece = Game.getBoard().getSquare(Game.getMove().getStart().x, Game.getMove().getStart().y)
                .getPiece();
        if (attackingPiece.getPieceType() instanceof Scout) {
            moveScoutBeforeAttack();
        }

        revealBattle();
        Thread.sleep(2000);
        fadeOutDeadPieces();
        Thread.sleep(1500);
    }

    private void moveScoutBeforeAttack() throws InterruptedException {
        int moveX = Game.getMove().getStart().x - Game.getMove().getEnd().x;
        int moveY = Game.getMove().getStart().y - Game.getMove().getEnd().y;

        if (Math.abs(moveX) > 1 || Math.abs(moveY) > 1) {
            Platform.runLater(() -> {
                try {
                    int shiftX = 0;
                    int shiftY = 0;

                    if (moveX > 0) {
                        shiftX = 1;
                    } else if (moveX < 0) {
                        shiftX = -1;
                    } else if (moveY > 0) {
                        shiftY = 1;
                    } else if (moveY < 0) {
                        shiftY = -1;
                    }

                    ClientSquare scoutSquare = Game.getBoard().getSquare(Game.getMove().getEnd().x + shiftX,
                            Game.getMove().getEnd().y + shiftY);
                    ClientSquare startSquare = Game.getBoard().getSquare(Game.getMove().getStart().x,
                            Game.getMove().getStart().y);

                    scoutSquare.getPiecePane()
                            .setPiece(HashTables.PIECE_MAP.get(startSquare.getPiece().getPieceSpriteKey()));
                    startSquare.getPiecePane().setPiece(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread.sleep(1000);

            int shiftX = 0;
            int shiftY = 0;

            if (moveX > 0) {
                shiftX = 1;
            } else if (moveX < 0) {
                shiftX = -1;
            } else if (moveY > 0) {
                shiftY = 1;
            } else if (moveY < 0) {
                shiftY = -1;
            }

            ClientSquare startSquare = Game.getBoard().getSquare(Game.getMove().getStart().x,
                    Game.getMove().getStart().y);
            Game.getBoard().getSquare(Game.getMove().getEnd().x + shiftX, Game.getMove().getEnd().y + shiftY)
                    .setPiece(startSquare.getPiece());
            Game.getBoard().getSquare(Game.getMove().getStart().x, Game.getMove().getStart().y).setPiece(null);
            Game.getMove().setStart(Game.getMove().getEnd().x + shiftX, Game.getMove().getEnd().y + shiftY);
        }
    }

    private void revealBattle() {
        Platform.runLater(() -> {
            try {
                ClientSquare startSquare = Game.getBoard().getSquare(Game.getMove().getStart().x,
                        Game.getMove().getStart().y);
                ClientSquare endSquare = Game.getBoard().getSquare(Game.getMove().getEnd().x,
                        Game.getMove().getEnd().y);

                startSquare.getPiecePane()
                        .setPiece(HashTables.PIECE_MAP.get(startSquare.getPiece().getPieceSpriteKey()));
                endSquare.getPiecePane().setPiece(HashTables.PIECE_MAP.get(endSquare.getPiece().getPieceSpriteKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void fadeOutDeadPieces() {
        Platform.runLater(() -> {
            try {
                ClientSquare startSquare = Game.getBoard().getSquare(Game.getMove().getStart().x,
                        Game.getMove().getStart().y);
                ClientSquare endSquare = Game.getBoard().getSquare(Game.getMove().getEnd().x,
                        Game.getMove().getEnd().y);

                if (!Game.getMove().isAttackWin()) {
                    FadeTransition fadeStart = new FadeTransition(Duration.millis(1500),
                            startSquare.getPiecePane().getPiece());
                    fadeStart.setFromValue(1.0);
                    fadeStart.setToValue(0.0);
                    fadeStart.play();
                    ResetImageVisibilityInterface fadeEndReset = new ResetImageVisibility();
                    fadeStart.setOnFinished(fadeEndReset);
                }

                if (!Game.getMove().isDefendWin()) {
                    FadeTransition fadeEnd = new FadeTransition(Duration.millis(1500),
                            endSquare.getPiecePane().getPiece());
                    fadeEnd.setFromValue(1.0);
                    fadeEnd.setToValue(0.0);
                    fadeEnd.play();
                    ResetImageVisibilityInterface fadeEndReset = new ResetImageVisibility();
                    fadeEnd.setOnFinished(fadeEndReset);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateSoftwareBoard(Piece startPiece, Piece endPiece) {
        Game.getBoard().getSquare(Game.getMove().getStart().x, Game.getMove().getStart().y).setPiece(startPiece);
        Game.getBoard().getSquare(Game.getMove().getEnd().x, Game.getMove().getEnd().y).setPiece(endPiece);
    }

    private void updateBoardsAndAnimations() throws InterruptedException {
        Platform.runLater(this::updateGUIAfterMove);

        if (Game.getMove().isAttackMove()) {
            Thread.sleep(50);
        }

        Platform.runLater(this::showArrowAnimation);
    }

    private void updateGUIAfterMove() {
        ClientSquare endSquare = Game.getBoard().getSquare(Game.getMove().getEnd().x, Game.getMove().getEnd().y);
        Piece endPiece = Game.getMove().getEndPiece();

        if (endPiece == null) {
            endSquare.getPiecePane().setPiece(null);
        } else {
            if (endPiece.getPieceColor() == Game.getPlayer().getColor()) {
                endSquare.getPiecePane().setPiece(HashTables.PIECE_MAP.get(endPiece.getPieceSpriteKey()));
            } else {
                if (endPiece.getPieceColor() == PieceColor.BLUE)
                    endSquare.getPiecePane().setPiece(ImageConstants.BLUE_BACK);
                else
                    endSquare.getPiecePane().setPiece(ImageConstants.RED_BACK);
            }
        }
    }

    private void showArrowAnimation() {
        ClientSquare arrowSquare = Game.getBoard().getSquare(Game.getMove().getStart().x, Game.getMove().getStart().y);

        if (Game.getMove().getMoveColor() == PieceColor.RED)
            arrowSquare.getPiecePane().setPiece(ImageConstants.MOVEARROW_RED);
        else
            arrowSquare.getPiecePane().setPiece(ImageConstants.MOVEARROW_BLUE);

        if (Game.getMove().getStart().x > Game.getMove().getEnd().x)
            arrowSquare.getPiecePane().getPiece().setRotate(0);
        else if (Game.getMove().getStart().y < Game.getMove().getEnd().y)
            arrowSquare.getPiecePane().getPiece().setRotate(90);
        else if (Game.getMove().getStart().x < Game.getMove().getEnd().x)
            arrowSquare.getPiecePane().getPiece().setRotate(180);
        else
            arrowSquare.getPiecePane().getPiece().setRotate(270);

        FadeTransition ft = new FadeTransition(Duration.millis(1500), arrowSquare.getPiecePane().getPiece());
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
        ResetSquareImageInterface resetSquareImage = new ResetSquareImage(waitFade);
        ft.setOnFinished(resetSquareImage);
    }

    private void waitFadeAnimation() throws InterruptedException {
        synchronized (waitFade) {
            waitFade.wait();
        }
    }

    private void revealAll() {
        Platform.runLater(() -> {
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    if (Game.getBoard().getSquare(row, col).getPiece() != null &&
                            Game.getBoard().getSquare(row, col).getPiece().getPieceColor() != Game.getPlayer()
                                    .getColor()) {
                        Game.getBoard().getSquare(row, col).getPiecePane().setPiece(
                                HashTables.PIECE_MAP
                                        .get(Game.getBoard().getSquare(row, col).getPiece().getPieceSpriteKey()));
                    }
                }
            }
        });
    }
}