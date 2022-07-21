package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("StartGameResponse")
public class StartGameResponse extends Command {
    private final boolean isGameStarted;
    //TODO Здесь передаем объект класса доски с текущем положением фигур
    private String errorMessage;

    public StartGameResponse(final boolean isGameStarted) {
        super(CommandType.START_GAME_RESPONSE);
        this.isGameStarted = isGameStarted;
    }

    @JsonCreator
    public StartGameResponse(
            @JsonProperty("isGameStarted") final boolean isGameStarted,
            @JsonProperty("errorMessage") final String errorMessage) {
        super(CommandType.START_GAME_RESPONSE);
        this.isGameStarted = isGameStarted;
        this.errorMessage = errorMessage;
    }

    public StartGameResponse() {
        super(CommandType.START_GAME_RESPONSE);
        this.isGameStarted = false;
    }

    @JsonProperty("isGameStarted")
    public boolean isGameStarted() {
        return isGameStarted;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "StartGameResponse{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", isGameStarted=" + isGameStarted +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
