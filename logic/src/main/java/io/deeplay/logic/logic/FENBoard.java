package io.deeplay.logic.logic;

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
