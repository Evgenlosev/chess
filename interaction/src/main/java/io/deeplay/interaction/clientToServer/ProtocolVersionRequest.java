package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("ProtocolVersionRequest")
public class ProtocolVersionRequest extends Command {
    private final String protocolVersion;

    public ProtocolVersionRequest(final String protocolVersion) {
        super(CommandType.PROTOCOL_VERSION_REQUEST);
        this.protocolVersion = protocolVersion;
    }

    public ProtocolVersionRequest() {
        super(CommandType.PROTOCOL_VERSION_REQUEST);
        this.protocolVersion = null;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public String toString() {
        return "ProtocolVersionRequest{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                '}';
    }
}
