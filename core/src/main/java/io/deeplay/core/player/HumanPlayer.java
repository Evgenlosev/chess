package io.deeplay.core.player;

import io.deeplay.core.model.*;

public class HumanPlayer extends Player {
    public HumanPlayer(final Side side, final int id) {
        super(side, id);
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.HUMAN;
    }

    //TODO: UI на вход принимает 2 координаты: от и куда. Нужен какой-то валидатор, который определит фигуру,
    // тип хода (MoveType) и вернет объект MoveInfo.
    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        throw new RuntimeException("Функционал еще не реализован");
    }
}
