package io.deeplay.core.model.bitboard;

import io.deeplay.core.logic.newlogic.QuadFunction;
import io.deeplay.core.model.Figure;
import io.deeplay.core.model.Side;

import java.util.Set;

// TODO: нужно ходы вычислять извне...
public class PieceBitboard {

    private final long occupied; // занятые клетки нужно знать, чтобы считать все возможные ходы
    private final Side side;
    private final Figure figure;
    private final int positionIndex;
    private final long positionBitboard;
    private final QuadFunction<ChessBitboard, Long, Long, Integer, Long> movesGenerationFunction;
    private long allMovesBitboard;
    private Set<MoveBitboard> allMoves; // TODO:
    /**
     * В данное поле добавляется информация о допустимых для хода клетках.
     */
    private long allRestrictionsBitboard;

    public PieceBitboard(final long occupied,
                         final Side side,
                         final Figure figure,
                         final int positionIndex,
                         final long positionBitboard,
                         final QuadFunction<ChessBitboard, Long, Long, Integer, Long> movesGenerationFunction) {
        this.occupied = occupied;
        this.side = side;
        this.figure = figure;
        this.positionIndex = positionIndex;
        this.positionBitboard = positionBitboard;
        this.movesGenerationFunction = movesGenerationFunction;
        this.allRestrictionsBitboard = ~0L;
    }

    public void initializeMoves(final ChessBitboard chessBitboard) {
        // ~0L - нету ограничений
        this.allMovesBitboard = movesGenerationFunction.apply(chessBitboard, 0L, ~0L, positionIndex);
    }

    public long getMoves(final ChessBitboard chessBitboard, final long ignorePiece, final long restriction) {
        return movesGenerationFunction.apply(chessBitboard, ignorePiece, restriction, positionIndex);
    }

    public long getMovesWithIgnoredPiece(final ChessBitboard chessBitboard, final long ignorePiece) {
        return getMoves(chessBitboard, ignorePiece, ~0L);
    }

    public long getMovesUnderRestrictions(final ChessBitboard chessBitboard) {
        return getMoves(chessBitboard, 0L, allRestrictionsBitboard);
    }
/*

    public List<MoveBitboard> getPossibleMoves() {
        if(allRestrictionsBitboard == ~0L)
            return allMoves;
        // TODO: иначе нужно исключить все неподходящие ходы
    }
*/

    public void addRestriction(final long restriction) {
        this.allRestrictionsBitboard &= restriction;
    }

    public long getOccupied() {
        return occupied;
    }

    public Side getSide() {
        return side;
    }

    public Figure getFigure() {
        return figure;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public long getPositionBitboard() {
        return positionBitboard;
    }

    public long getAllMovesBitboard() {
        return allMovesBitboard;
    }
}
