package io.deeplay.core.player;

import io.deeplay.core.model.*;

public class HumanPlayer extends Player{
    private final PlayerType playerType;

    public HumanPlayer(final Side side, final int id) {
        super(side, id);
        this.playerType = PlayerType.HUMAN;
    }

    //TODO: UI на вход принимает 2 координаты: от и куда. Нужен какой-то валидатор, который определит фигуру,
    // тип хода (MoveType) и вернет объект MoveInfo.
    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        return new MoveInfo(new Coord(5), new Coord(10), MoveType.USUAL_MOVE,
                Figure.W_PAWN);
    }
}
