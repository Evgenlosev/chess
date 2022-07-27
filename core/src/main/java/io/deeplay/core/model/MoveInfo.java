package io.deeplay.core.model;

import java.util.Objects;

public class MoveInfo {

    private final Coord cellFrom;
    private final Coord cellTo;
    private final MoveType moveType;
    private final Figure figure;
    private final Figure promoteTo;
    private final boolean checkedOpponent;
    private final boolean isMate; // TODO: достаточно GameStatus?

    public MoveInfo(final Coord cellFrom,
                    final Coord cellTo,
                    final MoveType moveType,
                    final Figure figure,
                    final Figure promoteTo,
                    final boolean checkedOpponent,
                    final boolean isMate) {
        this.cellFrom = cellFrom;
        this.cellTo = cellTo;
        this.moveType = moveType;
        this.figure = figure;
        this.promoteTo = promoteTo;
        this.checkedOpponent = checkedOpponent;
        this.isMate = isMate;
    }

    public MoveInfo(final Coord cellFrom, final Coord cellTo, final MoveType moveType, final Figure figure) {
        this(cellFrom, cellTo, moveType, figure, null, false, false);
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

    public Figure getPromoteTo() {
        return promoteTo;
    }

    public boolean getCheckedOpponent() {
        return checkedOpponent;
    }


    public boolean isMate() {
        return isMate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveInfo moveInfo = (MoveInfo) o;
        return isMate == moveInfo.isMate && Objects.equals(cellFrom, moveInfo.cellFrom) && Objects.equals(cellTo, moveInfo.cellTo) && moveType == moveInfo.moveType && figure == moveInfo.figure && promoteTo == moveInfo.promoteTo && checkedOpponent == moveInfo.checkedOpponent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellFrom, cellTo, moveType, figure, promoteTo, checkedOpponent, isMate);
    }

    @Override
    public String toString() {
        return "MoveInfo{" +
                "cellFrom=" + cellFrom +
                ", cellTo=" + cellTo +
                ", moveType=" + moveType +
                ", figure=" + figure +
                ", promoteTo=" + promoteTo +
                ", checkedOpponent=" + checkedOpponent +
                ", isMate=" + isMate +
                '}';
    }
}
