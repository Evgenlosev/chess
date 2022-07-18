package io.deeplay.clientToServer;

import io.deeplay.Command;
import io.deeplay.CommandType;

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
