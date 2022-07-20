package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("GameOverResponse")
public class GameOverResponse extends Command {
    private final boolean gameOvered;
    //Здесь предается информация о статусе окончания(кто выиграл или ничья)


    public GameOverResponse(final boolean gameOvered) {
        super(CommandType.GAME_OVER_RESPONSE);
        this.gameOvered = gameOvered;
    }

    public GameOverResponse() {
        super(CommandType.GAME_OVER_RESPONSE);
        this.gameOvered = false;
    }

    public boolean isGameOvered() {
        return gameOvered;
    }

    @Override
    public String toString() {
        return "GameOverResponse{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", gameOvered=" + gameOvered +
                '}';
    }
}
