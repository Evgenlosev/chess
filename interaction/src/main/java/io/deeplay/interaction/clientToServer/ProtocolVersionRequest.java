package io.deeplay.interaction.clientToServer;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

public class ProtocolVersionRequest extends Command {
    private final String protocolVersion;

    public ProtocolVersionRequest(final String protocolVersion) {
        super(CommandType.PROTOCOL_VERSION_REQUEST);
        this.protocolVersion = protocolVersion;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

}
