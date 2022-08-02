package io.deeplay.core.model;


public class BoardCell implements Cloneable {

    private Figure figure;

    /**
     * Переопределение, чтобы сделать метод публичным
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public BoardCell(Figure figure) {
        this.figure = figure;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(final Figure figure) {
        this.figure = figure;
    }
}

