package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("MoveRequest")
public class MoveRequest extends Command {
    private final int moveFrom;
    private final int moveTo;

    public MoveRequest(final int moveFrom, final int moveTo) {
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }

    public MoveRequest() {
        this.moveFrom = 0;
        this.moveTo = 0;
    }

    public int getMoveFrom() {
        return moveFrom;
    }

    public int getMoveTo() {
        return moveTo;
    }

    @JsonIgnore
    @Override
    public CommandType getCommandType() {
        return CommandType.MOVE_REQUEST;
    }

    @Override
    public String toString() {
        return "MoveRequest{" +
                "moveFrom=" + moveFrom +
                ", moveTo=" + moveTo +
                '}';
    }
}
