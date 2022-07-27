package io.deeplay.logic.logic;

import io.deeplay.core.model.Coord;
import io.deeplay.core.model.MoveType;
import io.deeplay.core.model.Side;
import io.deeplay.logic.model.ChessBitboard;

import java.util.HashMap;
import java.util.Map;

import static io.deeplay.logic.logic.BitUtils.*;


/**
 * Класс содержит методы, которые производят вычисления каждый раз когда нужно, в отличие от BitboardPatternsInitializer
 */
public class BitboardDynamicPatterns {

    // Взятие на проходе, в возможных атаках,не учитывается, т.к. невозможно (в классическом режиме) чтобы за пешкой,
    // которая только что походила на 2 клетки вперед, стояла какая-либо фигура
    public static long possibleWhitePawnAttacks(final Coord from) {
        final long pawnToMoveBitboard = 1L << from.getIndexAsOneDimension();

        long allAttackMoves = 0L;
        allAttackMoves |= (pawnToMoveBitboard << 9) & ~MASK_FILE_A; // capture right
        allAttackMoves |= (pawnToMoveBitboard << 7) & ~MASK_FILE_H; // capture left

        return allAttackMoves;
    }

    public static long possibleBlackPawnAttacks(final Coord from) {
        final long pawnToMoveBitboard = 1L << from.getIndexAsOneDimension();

        long allAttackMoves = 0L;
        allAttackMoves |= (pawnToMoveBitboard >>> 9) & ~MASK_FILE_H; // capture right
        allAttackMoves |= (pawnToMoveBitboard >>> 7) & ~MASK_FILE_A; // capture left
        return allAttackMoves;
    }

    public static Map<MoveType, Long> possibleWhitePawnMoves(final ChessBitboard chessBitboard, final Coord from) {
        if (chessBitboard.getMySide() != Side.WHITE)
            throw new IllegalArgumentException("Подсчёт ходов для белых пешек невозможен для чёрных пешек.");
        final long pawnToMoveBitboard = 1L << from.getIndexAsOneDimension();
        final long notMyPieces = chessBitboard.getOpponentPieces();
        final long opponentPawns = chessBitboard.getOpponentBitboards().getPawns();
        final long occupied = chessBitboard.getOccupied();
        final long empty = chessBitboard.getEmpty();
        final long enPassantFile = chessBitboard.getEnPassantFile();

        Map<MoveType, Long> moveTypes = new HashMap<>();
        long satisfyingMoves = (pawnToMoveBitboard << 9) & notMyPieces & occupied & ~MASK_RANK_8 & ~MASK_FILE_A; // capture right
        moveTypes.putIfAbsent(MoveType.PAWN_ATTACK, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard << 7) & notMyPieces & occupied & ~MASK_RANK_8 & ~MASK_FILE_H; // capture left
        moveTypes.put(MoveType.PAWN_ATTACK, moveTypes.get(MoveType.PAWN_ATTACK) | satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard << 8) & empty & ~MASK_RANK_8;// move 1 forward
        moveTypes.putIfAbsent(MoveType.USUAL_MOVE, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard << 16) & empty & (empty << 8) & MASK_RANK_4; // move 2 forward
        moveTypes.putIfAbsent(MoveType.PAWN_LONG_MOVE, satisfyingMoves);

        //Promotion
        satisfyingMoves = (pawnToMoveBitboard << 9) & notMyPieces & occupied & MASK_RANK_8 & ~MASK_FILE_A; // pawn promotion by capture right
        moveTypes.putIfAbsent(MoveType.PAWN_TO_FIGURE_ATTACK, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard << 7) & notMyPieces & occupied & MASK_RANK_8 & ~MASK_FILE_H; // pawn promotion by capture left
        moveTypes.put(MoveType.PAWN_TO_FIGURE_ATTACK, moveTypes.get(MoveType.PAWN_TO_FIGURE_ATTACK) | satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard << 8) & empty & MASK_RANK_8; // pawn promotion by move 1 forward
        moveTypes.putIfAbsent(MoveType.PAWN_TO_FIGURE, satisfyingMoves);

        if (enPassantFile != 0L) {
            // en passant right
            // shows destination, not piece to remove
            satisfyingMoves = (pawnToMoveBitboard << 9) & (opponentPawns << 8) & MASK_RANK_6 & ~MASK_FILE_A & enPassantFile;
            if (satisfyingMoves != 0) {
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
            // en passant left
            satisfyingMoves = (pawnToMoveBitboard << 7) & (opponentPawns << 8) & MASK_RANK_6 & ~MASK_FILE_H & enPassantFile;
            if (satisfyingMoves != 0) {
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
        }
        return moveTypes;
    }

    public static Map<MoveType, Long> possibleBlackPawnMoves(final ChessBitboard chessBitboard, final Coord from) {
        if (chessBitboard.getMySide() != Side.BLACK)
            throw new IllegalArgumentException("Подсчёт ходов для чёрных пешек невозможен для белых пешек.");
        final long pawnToMoveBitboard = 1L << from.getIndexAsOneDimension();
        final long notMyPieces = chessBitboard.getOpponentPieces();
        final long opponentPawns = chessBitboard.getOpponentBitboards().getPawns();
        final long occupied = chessBitboard.getOccupied();
        final long empty = chessBitboard.getEmpty();
        final long enPassantFile = chessBitboard.getEnPassantFile();

        Map<MoveType, Long> moveTypes = new HashMap<>();
        long satisfyingMoves = (pawnToMoveBitboard >>> 9) & notMyPieces & occupied & ~MASK_RANK_1 & ~MASK_FILE_H; // capture right
        moveTypes.putIfAbsent(MoveType.PAWN_ATTACK, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 7) & notMyPieces & occupied & ~MASK_RANK_1 & ~MASK_FILE_A; // capture left
        moveTypes.put(MoveType.PAWN_ATTACK, moveTypes.get(MoveType.PAWN_ATTACK) | satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 8) & empty & ~MASK_RANK_1; // move 1 forward
        moveTypes.putIfAbsent(MoveType.USUAL_MOVE, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 16) & empty & (empty >>> 8) & MASK_RANK_5; // move 2 forward
        moveTypes.putIfAbsent(MoveType.PAWN_LONG_MOVE, satisfyingMoves);

        //Promotion
        satisfyingMoves = (pawnToMoveBitboard >>> 9) & notMyPieces & occupied & MASK_RANK_1 & ~MASK_FILE_H; // pawn promotion by capture right
        moveTypes.putIfAbsent(MoveType.PAWN_TO_FIGURE_ATTACK, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 7) & notMyPieces & occupied & MASK_RANK_1 & ~MASK_FILE_A; // pawn promotion by capture left
        moveTypes.put(MoveType.PAWN_TO_FIGURE_ATTACK, moveTypes.get(MoveType.PAWN_TO_FIGURE_ATTACK) | satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 8) & empty & MASK_RANK_1;// pawn promotion by move 1 forward
        moveTypes.putIfAbsent(MoveType.PAWN_TO_FIGURE, satisfyingMoves);

        if (enPassantFile != 0L) {
            // en passant right
            satisfyingMoves = (pawnToMoveBitboard >>> 9) & (opponentPawns >>> 8) & MASK_RANK_3 & ~MASK_FILE_H & enPassantFile;
            if (satisfyingMoves != 0) {
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
            // en passant left
            satisfyingMoves = (pawnToMoveBitboard >>> 7) & (opponentPawns >>> 8) & MASK_RANK_3 & ~MASK_FILE_A & enPassantFile;
            if (satisfyingMoves != 0) {
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
        }
        return moveTypes;
    }


}
