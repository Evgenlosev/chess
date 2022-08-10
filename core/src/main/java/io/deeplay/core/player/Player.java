package io.deeplay.core.player;

import io.deeplay.core.listener.ChessAdapter;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;

public abstract class Player extends ChessAdapter {
    protected Side side;

    public Side getSide() {
        return side;
    }

    public abstract MoveInfo getAnswer(GameInfo gameInfo);

    @Override
    public String toString() {
        return side.toString();
    }
}
