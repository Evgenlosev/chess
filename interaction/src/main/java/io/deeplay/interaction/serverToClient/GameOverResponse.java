package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("GameOverResponse")
public class GameOverResponse extends Command {
    private final boolean isGameOvered;
    //Здесь предается информация о статусе окончания(кто выиграл или ничья)


    @JsonCreator
    public GameOverResponse(@JsonProperty("isGameOvered") final boolean isGameOvered) {
        super(CommandType.GAME_OVER_RESPONSE);
        this.isGameOvered = isGameOvered;
    }

    public GameOverResponse() {
        super(CommandType.GAME_OVER_RESPONSE);
        this.isGameOvered = false;
    }

    @JsonProperty("isGameOvered")
    public boolean isGameOvered() {
        return isGameOvered;
    }

    @Override
    public String toString() {
        return "GameOverResponse{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", isGameOvered=" + isGameOvered +
                '}';
    }
}
