package io.deeplay.clientToServer;

import io.deeplay.Command;
import io.deeplay.CommandType;


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
