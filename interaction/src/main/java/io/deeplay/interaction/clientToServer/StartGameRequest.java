package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.core.player.PlayerType;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("StartGameRequest")
public class StartGameRequest extends Command {
    private final PlayerType enemyPlayerType;
    //Помимо этого поля, описывающие характеристики игры

    public StartGameRequest(final PlayerType enemyPlayerType) {
        this.enemyPlayerType = enemyPlayerType;
    }

    public StartGameRequest() {
        this.enemyPlayerType = null;
    }

    public PlayerType getEnemyPlayerType() {
        return enemyPlayerType;
    }

    @JsonIgnore
    @Override
    public CommandType getCommandType() {
        return CommandType.START_GAME_REQUEST;
    }

    @Override
    public String toString() {
        return "StartGameRequest{" +
                "enemyPlayerType=" + enemyPlayerType +
                '}';
    }
}
