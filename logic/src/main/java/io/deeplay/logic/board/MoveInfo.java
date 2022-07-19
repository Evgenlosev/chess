package io.deeplay.logic.board;

public class MoveInfo {

    private final Coord cellFrom;
    private final Coord cellTo;
    private final MoveType moveType;
    private final Figure figure;

    public MoveInfo(final Coord cellFrom, final Coord cellTo, final MoveType moveType, final Figure figure) {
        this.cellFrom = cellFrom;
        this.cellTo = cellTo;
        this.moveType = moveType;
        this.figure = figure;
    }

    public Coord getCellFrom() {
        return cellFrom;
    }

    public Coord getCellTo() {
        return cellTo;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public Figure getFigure() {
        return figure;
    }
}
