package io.deeplay.core.player;

import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;

public abstract class Player {
    private Side side;
    private int id;

    public Player(final Side side, final int id) {
        this.side = side;
        this.id = id;
    }

    public Player(final Side side) {
        this.side = side;
        this.id = 0;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(final Side side) {
        this.side = side;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public abstract MoveInfo getAnswer(GameInfo gameInfo);

    public abstract PlayerType getPlayerType();

    @Override
    public String toString() {
        return getPlayerType() + " " + side;

    }
}
