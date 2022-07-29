package io.deeplay.core.logic;

public class FENBoard {

    private String fenNotation;

    public FENBoard(String fenNotation) {
        this.fenNotation = fenNotation;
    }

    public String getFenNotation() {
        return fenNotation;
    }

    public void setFenNotation(String fenNotation) {
        this.fenNotation = fenNotation;
    }
}
