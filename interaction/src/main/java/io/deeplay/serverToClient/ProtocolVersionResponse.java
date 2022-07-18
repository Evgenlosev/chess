package io.deeplay.serverToClient;

import io.deeplay.Command;
import io.deeplay.CommandType;

public class ProtocolVersionResponse extends Command {
    private final boolean isVersionMatch;
    private String errorMessage;

    public ProtocolVersionResponse(final boolean isVersionMatch) {
        super(CommandType.PROTOCOL_VERSION_RESPONSE);
        this.isVersionMatch = isVersionMatch;
    }

    public ProtocolVersionResponse(final boolean isVersionMatch, final String errorMessage) {
        super(CommandType.PROTOCOL_VERSION_RESPONSE);
        this.isVersionMatch = isVersionMatch;
        this.errorMessage = errorMessage;
    }

    public boolean isVersionMatch() {
        return isVersionMatch;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
