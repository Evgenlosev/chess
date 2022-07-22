package io.deeplay.logic.model;


public class BoardCell {

    private final boolean isOutOfBounds;
    private final boolean isEdge;

    private Figure figure;


    public BoardCell(boolean isOutOfBounds, boolean isEdge, Figure figure) {
        this.isOutOfBounds = isOutOfBounds;
        this.isEdge = isEdge;
        this.figure = figure;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(final Figure figure) {
        this.figure = figure;
    }

    public boolean isOutOfBounds() {
        return isOutOfBounds;
    }

    public boolean isEdge() {
        return isEdge;
    }
}

