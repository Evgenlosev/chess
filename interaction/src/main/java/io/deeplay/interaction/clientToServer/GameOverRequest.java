package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("GameOverRequest")
public class GameOverRequest extends Command {

    public GameOverRequest() {
        super(CommandType.GAME_OVER_REQUEST);
    }

    @Override
    public String toString() {
        return "GameOverRequest{" +
                "commandType='" + super.getCommandType() + '\'' +
                "}";
    }
}
