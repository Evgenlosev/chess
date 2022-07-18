package io.deeplay.interaction.clientToServer;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

public class StartGameRequest extends Command {
    private final int userId;
    //Помимо этого поля, описывающие характеристики игры


    public StartGameRequest(final int userId) {
        super(CommandType.START_GAME_REQUEST);
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
