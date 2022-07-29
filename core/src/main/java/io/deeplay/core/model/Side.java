package io.deeplay.core.model;

public enum Side {
    WHITE,
    BLACK;

    public static Side otherSide(final Side side){
        return side == WHITE ? BLACK : WHITE;
    }

}
