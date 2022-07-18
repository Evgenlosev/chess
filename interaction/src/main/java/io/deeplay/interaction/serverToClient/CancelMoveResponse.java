package io.deeplay.interaction.serverToClient;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

public class CancelMoveResponse extends Command {
    public CancelMoveResponse() {
        super(CommandType.CANCEL_MOVE_RESPONSE);
    }
}
