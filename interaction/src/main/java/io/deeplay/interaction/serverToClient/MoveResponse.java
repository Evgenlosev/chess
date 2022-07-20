package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("MoveResponse")
public class MoveResponse extends Command {
    private final boolean moveCorrect;
    //TODO Здесь передаем объект класса доски с текущем положением фигур

    public MoveResponse(final boolean moveCorrect) {
        super(CommandType.MOVE_RESPONSE);
        this.moveCorrect = moveCorrect;
    }

    public MoveResponse() {
        super(CommandType.MOVE_RESPONSE);
        this.moveCorrect = false;
    }

    public boolean isMoveCorrect() {
        return moveCorrect;
    }

    @Override
    public String toString() {
        return "MoveResponse{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", moveCorrect=" + moveCorrect +
                '}';
    }
}
