package io.deeplay.clientToServer;

import io.deeplay.Command;
import io.deeplay.CommandType;

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
