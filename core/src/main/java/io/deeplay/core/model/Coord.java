package io.deeplay.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.deeplay.core.logic.BitUtils;

import java.util.Objects;

public class Coord {
    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;

    /**
     * counts from 0 to 7 (actual - 1)
     */
    private int row;
    private int column;

    public Coord(final int indexAsOneDimension) {
        this.row = indexAsOneDimension / BOARD_WIDTH;
        this.column = indexAsOneDimension % BOARD_WIDTH;
    }

    public Coord(final int column, final int row) {
        this.row = row;
        this.column = column;
    }

    public Coord() {
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

    @JsonIgnore
    public int getIndexAsOneDimension() {
        return row * BOARD_WIDTH + column;
    }

    @Override
    public String toString() {
        return "Coord{" + BitUtils.SQUARES_STRING[getIndexAsOneDimension()] +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
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
