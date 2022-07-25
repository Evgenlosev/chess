package io.deeplay.logic;

import io.deeplay.core.model.Side;
import io.deeplay.model.ChessBitboard;
import io.deeplay.model.Coord;
import io.deeplay.model.MoveType;

import java.util.HashMap;
import java.util.Map;

import static io.deeplay.logic.BitUtils.*;


/**
 * Класс содержит методы, которые производят вычисления каждый раз когда нужно, в отличие от BitboardPatternsInitializer
 */
public class BitboardDynamicPatterns {

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

        satisfyingMoves = (pawnToMoveBitboard << 7) & notMyPieces & occupied & ~MASK_RANK_8 & ~MASK_FILE_H;//capture left
        moveTypes.put(MoveType.PAWN_ATTACK, moveTypes.get(MoveType.PAWN_ATTACK) | satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard << 8) & empty & ~MASK_RANK_8;//move 1 forward
        moveTypes.putIfAbsent(MoveType.USUAL_MOVE, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard << 16) & empty & (empty << 8) & MASK_RANK_4;//move 2 forward
        moveTypes.putIfAbsent(MoveType.PAWN_LONG_MOVE, satisfyingMoves);

        //Promotion
        satisfyingMoves = (pawnToMoveBitboard << 9) & notMyPieces & occupied & MASK_RANK_8 & ~MASK_FILE_A;//pawn promotion by capture right
        moveTypes.putIfAbsent(MoveType.PAWN_TO_FIGURE_ATTACK, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard << 7) & notMyPieces & occupied & MASK_RANK_8 & ~MASK_FILE_H;//pawn promotion by capture left
        moveTypes.put(MoveType.PAWN_TO_FIGURE_ATTACK, moveTypes.get(MoveType.PAWN_TO_FIGURE_ATTACK) | satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard << 8) & empty & MASK_RANK_8;//pawn promotion by move 1 forward
        moveTypes.putIfAbsent(MoveType.PAWN_TO_FIGURE, satisfyingMoves);

        if (enPassantFile != 0L) {
            //en passant right
            satisfyingMoves = (pawnToMoveBitboard << 9) & (opponentPawns << 8) & MASK_RANK_6 & ~MASK_FILE_A & enPassantFile;
            if (satisfyingMoves != 0) {
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
            //en passant left
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
        long satisfyingMoves = (pawnToMoveBitboard >>> 9) & notMyPieces & occupied & ~MASK_RANK_1 & ~MASK_FILE_H;//capture right
        moveTypes.putIfAbsent(MoveType.PAWN_ATTACK, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 7) & notMyPieces & occupied & ~MASK_RANK_1 & ~MASK_FILE_A;//capture left
        moveTypes.put(MoveType.PAWN_ATTACK, moveTypes.get(MoveType.PAWN_ATTACK) | satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 8) & empty & ~MASK_RANK_1;//move 1 forward
        moveTypes.putIfAbsent(MoveType.USUAL_MOVE, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 16) & empty & (empty >>> 8) & MASK_RANK_5;//move 2 forward
        moveTypes.putIfAbsent(MoveType.PAWN_LONG_MOVE, satisfyingMoves);

        //Promotion
        satisfyingMoves = (pawnToMoveBitboard >>> 9) & notMyPieces & occupied & MASK_RANK_1 & ~MASK_FILE_H;//pawn promotion by capture right
        moveTypes.putIfAbsent(MoveType.PAWN_TO_FIGURE_ATTACK, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 7) & notMyPieces & occupied & MASK_RANK_1 & ~MASK_FILE_A;//pawn promotion by capture left
        moveTypes.put(MoveType.PAWN_TO_FIGURE_ATTACK, moveTypes.get(MoveType.PAWN_TO_FIGURE_ATTACK) | satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 8) & empty & MASK_RANK_1;//pawn promotion by move 1 forward
        moveTypes.putIfAbsent(MoveType.PAWN_TO_FIGURE, satisfyingMoves);

        if (enPassantFile != 0L) {
            //en passant right
            satisfyingMoves = (pawnToMoveBitboard >>> 9) & (opponentPawns >>> 8) & MASK_RANK_3 & ~MASK_FILE_H & enPassantFile;//shows piece to remove, not the destination
            if (satisfyingMoves != 0) {
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
            //en passant left
            satisfyingMoves = (pawnToMoveBitboard >>> 7) & (opponentPawns >>> 8) & MASK_RANK_3 & ~MASK_FILE_A & enPassantFile;//shows piece to remove, not the destination
            if (satisfyingMoves != 0) {
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
        }
        return moveTypes;
    }


}
