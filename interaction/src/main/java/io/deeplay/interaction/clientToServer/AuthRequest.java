package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("AuthRequest")
public class AuthRequest extends Command {
    private final String userName;

    public AuthRequest(final String userName) {
        super(CommandType.AUTH_REQUEST);
        this.userName = userName;
    }

    public AuthRequest() {
        super(CommandType.AUTH_REQUEST);
        this.userName = null;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
