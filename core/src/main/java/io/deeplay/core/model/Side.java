package io.deeplay.core.model;

public enum Side {
    WHITE,
    BLACK;

    public static Side otherSide(Side side){
        return side == WHITE ? BLACK : WHITE;
    }
}
