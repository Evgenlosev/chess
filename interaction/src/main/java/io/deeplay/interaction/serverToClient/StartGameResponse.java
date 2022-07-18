package io.deeplay.interaction.serverToClient;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

public class StartGameResponse extends Command {
    private final boolean isGameStarted;
    //TODO Здесь передаем объект класса доски с текущем положением фигур
    private String errorMessage;

    public StartGameResponse(final boolean isGameStarted) {
        super(CommandType.START_GAME_REQUEST);
        this.isGameStarted = isGameStarted;
    }

    public StartGameResponse(final boolean isGameStarted, final String errorMessage) {
        super(CommandType.START_GAME_RESPONSE);
        this.isGameStarted = isGameStarted;
        this.errorMessage = errorMessage;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
