package io.deeplay.interaction.serverToClient;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

public class GameOverResponse extends Command {
    private final boolean isGameOvered;
    //Здесь предается информация о статусе окончания(кто выиграл или ничья)


    public GameOverResponse(final boolean isGameOvered) {
        super(CommandType.GAME_OVER_RESPONSE);
        this.isGameOvered = isGameOvered;
    }

    public boolean isGameOvered() {
        return isGameOvered;
    }
}
