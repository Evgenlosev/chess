package io.deeplay.interaction.clientToServer;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

public class GameOverRequest extends Command {

    public GameOverRequest() {
        super(CommandType.GAME_OVER_REQUEST);
    }
}
