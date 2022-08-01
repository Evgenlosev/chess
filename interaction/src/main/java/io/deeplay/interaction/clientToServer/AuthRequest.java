package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("AuthRequest")
public class AuthRequest extends Command {
    private final String userName;

    public AuthRequest(final String userName) {
        this.userName = userName;
    }

    public AuthRequest() {
        this.userName = null;
    }

    @JsonIgnore
    @Override
    public CommandType getCommandType() {
        return CommandType.AUTH_REQUEST;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
