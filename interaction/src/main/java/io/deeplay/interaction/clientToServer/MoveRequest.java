package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("MoveRequest")
public class MoveRequest extends Command {
    private final int moveFrom;
    private final int moveTo;

    public MoveRequest(final int moveFrom, final int moveTo) {
        super(CommandType.MOVE_REQUEST);
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }

    public MoveRequest() {
        super(CommandType.MOVE_REQUEST);
        this.moveFrom = 0;
        this.moveTo = 0;
    }

    public int getMoveFrom() {
        return moveFrom;
    }

    public int getMoveTo() {
        return moveTo;
    }

    @Override
    public String toString() {
        return "MoveRequest{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", moveFrom=" + moveFrom +
                ", moveTo=" + moveTo +
                '}';
    }
}
