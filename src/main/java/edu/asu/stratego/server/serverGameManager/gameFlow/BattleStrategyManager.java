package edu.asu.stratego.server.serverGameManager.gameFlow;

import java.util.HashMap;
import java.util.Map;

import edu.asu.stratego.common.game.BattleOutcome;
import edu.asu.stratego.common.game.Move;
import edu.asu.stratego.common.game.Piece;
import edu.asu.stratego.server.serverGameManager.boardManager.BoardManagerInterface;

public class BattleStrategyManager {
    private final Map<BattleOutcome, BattleResolutionStrategy> strategies;

    public BattleStrategyManager() {
        strategies = new HashMap<>();
        strategies.put(BattleOutcome.WIN, new WinStrategy());
        strategies.put(BattleOutcome.LOSE, new LoseStrategy());
        strategies.put(BattleOutcome.DRAW, new DrawStrategy());
    }

    public void executeStrategy(BattleOutcome outcome, Move moveToPlayerOne, Move moveToPlayerTwo, Move move, Piece attacker, Piece defender, BoardManagerInterface board) {
        BattleResolutionStrategy strategy = strategies.get(outcome);
        if (strategy != null) {
            strategy.resolve(moveToPlayerOne, moveToPlayerTwo, move, attacker, defender, board);
        } else {
            throw new IllegalArgumentException("No strategy for outcome: " + outcome);
        }
    }
}