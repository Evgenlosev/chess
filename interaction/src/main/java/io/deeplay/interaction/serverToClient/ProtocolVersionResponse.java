package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("ProtocolVersionResponse")
public class ProtocolVersionResponse extends Command {
    private final boolean versionMatch;
    private String errorMessage;

    public ProtocolVersionResponse(final boolean versionMatch) {
        super(CommandType.PROTOCOL_VERSION_RESPONSE);
        this.versionMatch = versionMatch;
    }

    public ProtocolVersionResponse(final boolean versionMatch, final String errorMessage) {
        super(CommandType.PROTOCOL_VERSION_RESPONSE);
        this.versionMatch = versionMatch;
        this.errorMessage = errorMessage;
    }

    public ProtocolVersionResponse() {
        super(CommandType.PROTOCOL_VERSION_RESPONSE);
        this.versionMatch = false;
    }

    public boolean isVersionMatch() {
        return versionMatch;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ProtocolVersionResponse{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", versionMatch=" + versionMatch +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
