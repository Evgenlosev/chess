package io.deeplay.clientToServer;

import io.deeplay.Command;
import io.deeplay.CommandType;

public class CancelMoveRequest extends Command {
    public CancelMoveRequest() {
        super(CommandType.CANCEL_MOVE_REQUEST);
    }
}
