package io.deeplay.core.logic;

import io.deeplay.core.model.MoveType;
import io.deeplay.core.model.Side;
import io.deeplay.core.model.bitboard.ChessBitboard;
import io.deeplay.core.model.bitboard.SideBitboards;

import java.util.HashMap;
import java.util.Map;

import static io.deeplay.core.logic.BitUtils.*;


/**
 * Класс содержит методы, которые производят вычисления каждый раз когда нужно, в отличие от BitboardPatternsInitializer
 */
public class BitboardDynamicPatterns {

    // TODO: удалить не нужные методы
    public static Map<MoveType, Long> possibleWhitePawnMoves(final ChessBitboard chessBitboard, final int from) {
        if (chessBitboard.getProcessingSide() != Side.WHITE)
            throw new IllegalArgumentException("Подсчёт ходов для белых пешек невозможен для чёрных пешек.");
        final long pawnToMoveBitboard = 1L << from;
        final long notMyPieces = chessBitboard.getOpponentPieces();
        final long opponentPawns = chessBitboard.getOpponentSideBitboards().getPawns();
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

    public static Map<MoveType, Long> possibleBlackPawnMoves(final ChessBitboard chessBitboard, final int from) {
        if (chessBitboard.getProcessingSide() != Side.BLACK)
            throw new IllegalArgumentException("Подсчёт ходов для чёрных пешек невозможен для белых пешек.");
        final long pawnToMoveBitboard = 1L << from;
        final long notMyPieces = chessBitboard.getOpponentPieces();
        final long opponentPawns = chessBitboard.getOpponentSideBitboards().getPawns();
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

    public static long possibleWhitePawnMovesBitboard(final ChessBitboard chessBitboard, final int from) {
        SideBitboards blackSideBitboard = chessBitboard.getOpponentSideBitboards();
        if (blackSideBitboard.getSide() != Side.BLACK) {
            blackSideBitboard = chessBitboard.getProcessingSideBitboards();
        }
        final long pawnToMoveBitboard = 1L << from;
        final long blackPieces = blackSideBitboard.orOperationOnAllBitboards();
        final long blackPawns = blackSideBitboard.getPawns();
        final long occupied = chessBitboard.getOccupied();
        final long empty = chessBitboard.getEmpty();
        final long enPassantFile = chessBitboard.getEnPassantFile();

        long allMoves = (pawnToMoveBitboard << 9) & blackPieces & occupied & ~MASK_RANK_8 & ~MASK_FILE_A;
        allMoves |= (pawnToMoveBitboard << 7) & blackPieces & occupied & ~MASK_RANK_8 & ~MASK_FILE_H;
        allMoves |= (pawnToMoveBitboard << 8) & empty & ~MASK_RANK_8;
        allMoves |= (pawnToMoveBitboard << 16) & empty & (empty << 8) & MASK_RANK_4;
        allMoves |= (pawnToMoveBitboard << 9) & blackPieces & occupied & MASK_RANK_8 & ~MASK_FILE_A;
        allMoves |= (pawnToMoveBitboard << 7) & blackPieces & occupied & MASK_RANK_8 & ~MASK_FILE_H;
        allMoves |= (pawnToMoveBitboard << 8) & empty & MASK_RANK_8;
        if (enPassantFile != 0L) {
            allMoves |= (pawnToMoveBitboard << 9) & (blackPawns << 8) & MASK_RANK_6 & ~MASK_FILE_A & enPassantFile;
            allMoves |= (pawnToMoveBitboard << 7) & (blackPawns << 8) & MASK_RANK_6 & ~MASK_FILE_H & enPassantFile;
        }
        return allMoves;
    }

    public static long possibleBlackPawnMovesBitboard(final ChessBitboard chessBitboard, final int from) {
        SideBitboards whiteSideBitboard = chessBitboard.getOpponentSideBitboards();
        if (whiteSideBitboard.getSide() != Side.WHITE) {
            whiteSideBitboard = chessBitboard.getProcessingSideBitboards();
        }
        final long pawnToMoveBitboard = 1L << from;
        final long whitePieces = whiteSideBitboard.orOperationOnAllBitboards();
        final long whitePawns = whiteSideBitboard.getPawns();
        final long occupied = chessBitboard.getOccupied();
        final long empty = chessBitboard.getEmpty();
        final long enPassantFile = chessBitboard.getEnPassantFile();

        long allMoves = (pawnToMoveBitboard >>> 9) & whitePieces & occupied & ~MASK_RANK_1 & ~MASK_FILE_H; // capture right
        allMoves |= (pawnToMoveBitboard >>> 7) & whitePieces & occupied & ~MASK_RANK_1 & ~MASK_FILE_A; // capture left
        allMoves |= (pawnToMoveBitboard >>> 8) & empty & ~MASK_RANK_1; // move 1 forward
        allMoves |= (pawnToMoveBitboard >>> 16) & empty & (empty >>> 8) & MASK_RANK_5; // move 2 forward
        allMoves |= (pawnToMoveBitboard >>> 9) & whitePieces & occupied & MASK_RANK_1 & ~MASK_FILE_H; // pawn promotion by capture right
        allMoves |= (pawnToMoveBitboard >>> 7) & whitePieces & occupied & MASK_RANK_1 & ~MASK_FILE_A; // pawn promotion by capture left
        allMoves |= (pawnToMoveBitboard >>> 8) & empty & MASK_RANK_1;// pawn promotion by move 1 forward
        if (enPassantFile != 0L) {
            allMoves |= (pawnToMoveBitboard >>> 9) & (whitePawns >>> 8) & MASK_RANK_3 & ~MASK_FILE_H & enPassantFile;
            allMoves |= (pawnToMoveBitboard >>> 7) & (whitePawns >>> 8) & MASK_RANK_3 & ~MASK_FILE_A & enPassantFile;
        }
        return allMoves;
    }
}
