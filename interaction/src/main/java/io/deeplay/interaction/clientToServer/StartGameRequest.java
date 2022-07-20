package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("StartGameRequest")
public class StartGameRequest extends Command {
    private final int userId;
    //Помимо этого поля, описывающие характеристики игры


    public StartGameRequest(final int userId) {
        super(CommandType.START_GAME_REQUEST);
        this.userId = userId;
    }

    public StartGameRequest() {
        super(CommandType.START_GAME_REQUEST);
        this.userId = 0;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "StartGameRequest{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", userId=" + userId +
                '}';
    }
}
