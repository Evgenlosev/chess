package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("StartGameRequest")
public class StartGameRequest extends Command {
    private final int userId;
    //Помимо этого поля, описывающие характеристики игры

    public StartGameRequest(final int userId) {
        this.userId = userId;
    }

    public StartGameRequest() {
        this.userId = 0;
    }

    public int getUserId() {
        return userId;
    }

    @JsonIgnore
    @Override
    public CommandType getCommandType() {
        return CommandType.START_GAME_REQUEST;
    }

    @Override
    public String toString() {
        return "StartGameRequest{" +
                "userId=" + userId +
                '}';
    }
}
