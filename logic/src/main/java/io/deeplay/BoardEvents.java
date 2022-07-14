package io.deeplay;

import io.deeplay.model.ChessBoard;
import io.deeplay.settings.BoardSettings;

public interface BoardEvents {

    /**
     * @param from изначальная позиция фигуры в одномерном массиве доски от 0 до 63
     * @param to конечная позиция фигуры
     */
    void move(int from, int to);

    // TODO: нужен enum Piece из модуля core
    //  void move(int from, int to, Piece promoteTo);

}
