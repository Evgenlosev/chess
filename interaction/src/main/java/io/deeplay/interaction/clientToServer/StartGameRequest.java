package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("StartGameRequest")
public class StartGameRequest extends Command {
    public StartGameRequest() {
    }

    @JsonIgnore
    @Override
    public CommandType getCommandType() {
        return CommandType.START_GAME_REQUEST;
    }

    @Override
    public String toString() {
        return "StartGameRequest{" +
                '}';
    }
}
