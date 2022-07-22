package io.deeplay.model;

import java.util.Objects;

public class Coord {
    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;

    private int row;
    private int column;

    public Coord(int indexAsOneDimension){
        this.row = indexAsOneDimension / BOARD_WIDTH;
        this.column = indexAsOneDimension % BOARD_WIDTH;
    }

    public int getRow() {
        return row;
    }

    public void setRow(final int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(final int column) {
        this.column = column;
    }

    public int getIndexAsOneDimension(){
        return row * BOARD_WIDTH + column;
    }

    @Override
    public String toString() {
        return "Coord{" + getIndexAsOneDimension() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return row == coord.row && column == coord.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}

