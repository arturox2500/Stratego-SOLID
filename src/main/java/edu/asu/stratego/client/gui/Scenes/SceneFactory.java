package edu.asu.stratego.client.gui.Scenes;

import edu.asu.stratego.client.gui.Scenes.ConnectionScene.ConnectToServerInterface;
import edu.asu.stratego.client.gui.Scenes.ConnectionScene.ConnectionScene;
import edu.asu.stratego.client.gui.board.setup.SelectPieceInterface;
import edu.asu.stratego.client.gui.board.setup.SetupTimer.SetupTimerInterface;
import edu.asu.stratego.common.chat.ChatController;

public class SceneFactory {
    public static BaseScene createScene(String sceneName, Object... args) {
        switch (sceneName) {
            case "ConnectionScene":
                return new ConnectionScene();
            case "WaitingScene":
                return new WaitingScene();
            case "BoardScene":
                return new BoardScene((SetupTimerInterface) args[0], (SelectPieceInterface) args[1], (String) args[2],(ChatController) args[3], (ConnectToServerInterface) args[4]); // Assuming args[0] is of type SetupTimerInterface
            case "FinalScene":
                return new FinalScene(args);
            default:
                throw new IllegalArgumentException("Unknown scene: " + sceneName);
        }
    }
}
