package io.deeplay;

public class MagicBoard {

    /**
     * блокируемая маска содержащий все клетки, которые могут блокировать линейноходящие фигуры
     * A bitboard containing all squares that can block a rook or a bishop.
     */
    public long blockerMask;

    /**
     * Сгенерированные доски передвижений
     * The generated move boards.
     */
    public long[] moveBoards;

    /**
     * Предварительно вычисленное значение для того чтобы определить корректный индекс доски передвижений
     * Precomputed value to determine the correct move board index.
     */
    public int shift;

}
