package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("GameOverRequest")
public class GameOverRequest extends Command {

    public GameOverRequest() {
    }

    @JsonIgnore
    @Override
    public CommandType getCommandType() {
        return CommandType.GAME_OVER_REQUEST;
    }

    @Override
    public String toString() {
        return "GameOverRequest{" +
                "}";
    }
}
