package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("StartGameResponse")
public class StartGameResponse extends Command {
    private final boolean gameStarted;
    //TODO Здесь передаем объект класса доски с текущем положением фигур
    private String errorMessage;

    public StartGameResponse(final boolean gameStarted) {
        super(CommandType.START_GAME_RESPONSE);
        this.gameStarted = gameStarted;
    }
    public StartGameResponse(final boolean gameStarted, final String errorMessage) {
        super(CommandType.START_GAME_RESPONSE);
        this.gameStarted = gameStarted;
        this.errorMessage = errorMessage;
    }

    public StartGameResponse() {
        super(CommandType.START_GAME_RESPONSE);
        this.gameStarted = false;
    }

    public boolean isGameStarted() {
        return gameStarted;
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
                ", gameStarted=" + gameStarted +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
