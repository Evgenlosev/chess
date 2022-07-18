package io.deeplay.serverToClient;

import io.deeplay.Command;
import io.deeplay.CommandType;

public class CancelMoveResponse extends Command {
    public CancelMoveResponse() {
        super(CommandType.CANCEL_MOVE_RESPONSE);
    }
}
