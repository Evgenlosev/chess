package io.deeplay.interaction.clientToServer;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

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
