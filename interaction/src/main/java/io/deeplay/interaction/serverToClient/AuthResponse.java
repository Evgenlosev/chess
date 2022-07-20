package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("AuthResponse")
public class AuthResponse extends Command {
    private final boolean authorized;
    private final int userId;
    private String errorMessage;

    public AuthResponse(final boolean authorized, final int userId, final String errorMessage) {
        super(CommandType.AUTH_RESPONSE);
        this.authorized = authorized;
        this.userId = userId;
        this.errorMessage = errorMessage;
    }

    public AuthResponse(final boolean authorized, final int userId) {
        super(CommandType.AUTH_RESPONSE);
        this.authorized = authorized;
        this.userId = userId;
    }

    public AuthResponse() {
        super(CommandType.AUTH_RESPONSE);
        this.authorized = false;
        this.userId = 0;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public int getUserId() {
        return userId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", authorized=" + authorized +
                ", userId=" + userId +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
