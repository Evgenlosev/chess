package io.deeplay.logic;

public class ChessBoard {

    private String fenNotation;

    public ChessBoard(String fenNotation) {
        this.fenNotation = fenNotation;
    }

    public String getFenNotation() {
        return fenNotation;
    }

    public void setFenNotation(String fenNotation) {
        this.fenNotation = fenNotation;
    }
}
