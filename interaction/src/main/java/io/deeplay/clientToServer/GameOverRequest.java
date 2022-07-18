package io.deeplay.clientToServer;

import io.deeplay.Command;
import io.deeplay.CommandType;

public class GameOverRequest extends Command {

    public GameOverRequest() {
        super(CommandType.GAME_OVER_REQUEST);
    }
}
