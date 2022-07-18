package io.deeplay.interaction.serverToClient;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

public class AuthResponse extends Command {
    private final boolean isAuthorized;
    private final int userId;
    private String errorMessage;

    public AuthResponse(final boolean isAuthorized, final int userId) {
        super(CommandType.AUTH_RESPONSE);
        this.isAuthorized = isAuthorized;
        this.userId = userId;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public int getId() {
        return userId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
