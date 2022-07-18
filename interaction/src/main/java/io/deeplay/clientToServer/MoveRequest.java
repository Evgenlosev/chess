package io.deeplay.clientToServer;

import io.deeplay.Command;
import io.deeplay.CommandType;

public class MoveRequest extends Command {
    private final int moveFrom;
    private final int moveTo;

    public MoveRequest(final int moveFrom, final int moveTo) {
        super(CommandType.MOVE_REQUEST);
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }

    public int getMoveFrom() {
        return moveFrom;
    }

    public int getMoveTo() {
        return moveTo;
    }
}
