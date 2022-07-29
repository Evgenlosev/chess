package io.deeplay.core.model;

public enum Side {
    WHITE("w"),
    BLACK("b");

    public String toString() {
        return side;
    }

    private final String side;

    Side(String side) {
        this.side = side;
    }

    public static Side otherSide(Side side){
        return side == WHITE ? BLACK : WHITE;
    }
}
