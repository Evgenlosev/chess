package io.deeplay.logic.board;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "MoveInfo{" +
                "cellFrom=" + cellFrom +
                ", cellTo=" + cellTo +
                ", moveType=" + moveType +
                ", figure=" + figure +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveInfo moveInfo = (MoveInfo) o;
        return Objects.equals(cellFrom, moveInfo.cellFrom) && Objects.equals(cellTo, moveInfo.cellTo) && moveType == moveInfo.moveType && figure == moveInfo.figure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellFrom, cellTo, moveType, figure);
    }
}
