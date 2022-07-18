package io.deeplay.interaction.clientToServer;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

public class CancelMoveRequest extends Command {
    public CancelMoveRequest() {
        super(CommandType.CANCEL_MOVE_REQUEST);
    }
}
