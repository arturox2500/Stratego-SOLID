package edu.asu.stratego.server.serverGameManager.playerManager;

import edu.asu.stratego.common.game.Player;

public interface PlayerManagerInterface {
    void exchangePlayers();
    Player getPlayerOne();
    Player getPlayerTwo();
}
