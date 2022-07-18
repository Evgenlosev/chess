package io.deeplay.interaction.clientToServer;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;


public class AuthRequest extends Command {
    private final String userName;

    public AuthRequest(final String userName) {
        super(CommandType.AUTH_REQUEST);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
