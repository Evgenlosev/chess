package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("ProtocolVersionResponse")
public class ProtocolVersionResponse extends Command {
    private final boolean isVersionMatch;
    private String errorMessage;

    public ProtocolVersionResponse(final boolean isVersionMatch) {
        super(CommandType.PROTOCOL_VERSION_RESPONSE);
        this.isVersionMatch = isVersionMatch;
    }
    @JsonCreator
    public ProtocolVersionResponse(
            @JsonProperty("isVersionMatch") final boolean isVersionMatch,
            @JsonProperty("errorMessage") final String errorMessage) {
        super(CommandType.PROTOCOL_VERSION_RESPONSE);
        this.isVersionMatch = isVersionMatch;
        this.errorMessage = errorMessage;
    }

    public ProtocolVersionResponse() {
        super(CommandType.PROTOCOL_VERSION_RESPONSE);
        this.isVersionMatch = false;
    }

    @JsonProperty("isVersionMatch")
    public boolean isVersionMatch() {
        return isVersionMatch;
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
                ", isVersionMatch=" + isVersionMatch +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
