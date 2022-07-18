package io.deeplay.serverToClient;

import io.deeplay.Command;
import io.deeplay.CommandType;

public class MoveResponse extends Command {
    private final boolean isMoveCorrect;
    //TODO Здесь передаем объект класса доски с текущем положением фигур



    public MoveResponse(final boolean isMoveCorrect) {
        super(CommandType.CANCEL_MOVE_RESPONSE);
        this.isMoveCorrect = isMoveCorrect;
    }

    public boolean isMoveCorrect() {
        return isMoveCorrect;
    }
}
