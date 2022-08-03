package io.deeplay.core.player;


import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;

public class HumanPlayer extends Player {
    public HumanPlayer(final Side side) {
        super(side);
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.HUMAN;
    }

    //TODO: UI на вход принимает 2 координаты: от и куда. Нужен какой-то валидатор, который определит фигуру,
    // тип хода (MoveType) и вернет объект MoveInfo.
    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        throw new RuntimeException("Human move has not been done yet");
    }
}
