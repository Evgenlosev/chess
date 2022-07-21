package io.deeplay.logic;

import io.deeplay.core.model.Side;
import io.deeplay.logic.board.Coord;
import io.deeplay.logic.board.MoveType;

import java.util.HashMap;
import java.util.Map;

import static io.deeplay.logic.BitUtils.*;


/**
 * Класс содержит методы, которые производят вычисления каждый раз когда нужно, в отличие от BitboardPatternsInitializer
 */
public class BitboardDynamicPatterns {

    // TODO: тест когда 2 фигуры походили на 2 клетки, но только 1 походила на 2 вперед прошлым ходом
    public static Map<MoveType, Long> possibleWhitePawnMoves(final ChessBitboard chessBitboard, final Coord from) {
        if(chessBitboard.getMySide() != Side.WHITE)
            throw new IllegalArgumentException("Подсчёт ходов для белых пешек невозможен для чёрных пешек.");
        final long pawnToMoveBitboard = 1L << from.getIndexAsOneDimension();
        final long notMyPieces = chessBitboard.getOpponentPieces();
        final long opponentPawns = chessBitboard.getOpponentPawns();
        final long occupied = chessBitboard.getOccupied();
        final long empty = chessBitboard.getEmpty();
        final boolean isEnPassant = chessBitboard.isEnPassant();// TODO: true, если в нотации что то написано, это нужно узнавать из нотации FEN
        final long enPassantFile = chessBitboard.getEnPassantFile(); // FILE(вертикаль) с единицами на котором возможно взятие на проходе

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

        // TODO:
        if(isEnPassant) {
            //en passant right
            satisfyingMoves = (pawnToMoveBitboard >>> 1) & opponentPawns & MASK_RANK_5 & ~MASK_FILE_A & enPassantFile;//shows piece to remove, not the destination
            if (satisfyingMoves != 0) {
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
            //en passant left
            satisfyingMoves = (pawnToMoveBitboard << 1) & opponentPawns & MASK_RANK_5 & ~MASK_FILE_H & enPassantFile;//shows piece to remove, not the destination
            if (satisfyingMoves != 0) {
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
        }
        return moveTypes;
    }

    public static Map<MoveType, Long> possibleBlackPawnMoves(final ChessBitboard chessBitboard, final Coord from) {
        if(chessBitboard.getMySide() != Side.BLACK)
            throw new IllegalArgumentException("Подсчёт ходов для чёрных пешек невозможен для белых пешек.");
        final long pawnToMoveBitboard = 1L << from.getIndexAsOneDimension();
        final long notMyPieces = chessBitboard.getOpponentPieces();
        final long opponentPawns = chessBitboard.getOpponentPawns();
        final long occupied = chessBitboard.getOccupied();
        final long empty = chessBitboard.getEmpty();
        final boolean isEnPassant = chessBitboard.isEnPassant();
        final long enPassantFile = chessBitboard.getEnPassantFile();

        Map<MoveType, Long> moveTypes = new HashMap<>();
        long possibleMoves = 0L;
        long satisfyingMoves = (pawnToMoveBitboard >>> 9) & notMyPieces & occupied & ~MASK_RANK_1 & ~MASK_FILE_H;//capture right
        possibleMoves |= satisfyingMoves;
        moveTypes.putIfAbsent(MoveType.PAWN_ATTACK, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 7) & notMyPieces & occupied & ~MASK_RANK_1 & ~MASK_FILE_A;//capture left
        possibleMoves |= satisfyingMoves;
        moveTypes.put(MoveType.PAWN_ATTACK, moveTypes.get(MoveType.PAWN_ATTACK) | satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 8) & empty & ~MASK_RANK_1;//move 1 forward
        possibleMoves |= satisfyingMoves;
        moveTypes.putIfAbsent(MoveType.USUAL_MOVE, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 16) & empty & (empty >>> 8) & MASK_RANK_5;//move 2 forward
        possibleMoves |= satisfyingMoves;
        moveTypes.putIfAbsent(MoveType.PAWN_LONG_MOVE, satisfyingMoves);

        //Promotion
        satisfyingMoves = (pawnToMoveBitboard >>> 9) & notMyPieces & occupied & MASK_RANK_1 & ~MASK_FILE_H;//pawn promotion by capture right
        possibleMoves |= satisfyingMoves;
        moveTypes.putIfAbsent(MoveType.PAWN_TO_FIGURE_ATTACK, satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 7) & notMyPieces & occupied & MASK_RANK_1 & ~MASK_FILE_A;//pawn promotion by capture left
        possibleMoves |= satisfyingMoves;
        moveTypes.put(MoveType.PAWN_TO_FIGURE_ATTACK, moveTypes.get(MoveType.PAWN_TO_FIGURE_ATTACK) | satisfyingMoves);

        satisfyingMoves = (pawnToMoveBitboard >>> 8) & empty & MASK_RANK_1;//pawn promotion by move 1 forward
        possibleMoves |= satisfyingMoves;
        moveTypes.putIfAbsent(MoveType.PAWN_TO_FIGURE, satisfyingMoves);

        // TODO:
        if(isEnPassant) {
            //en passant right
            satisfyingMoves = (pawnToMoveBitboard << 1) & opponentPawns & MASK_RANK_4 & ~MASK_FILE_H & enPassantFile;//shows piece to remove, not the destination
            if (satisfyingMoves != 0) {
                possibleMoves |= satisfyingMoves;
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
            //en passant left
            satisfyingMoves = (pawnToMoveBitboard >>> 1) & opponentPawns & MASK_RANK_4 & ~MASK_FILE_A & enPassantFile;//shows piece to remove, not the destination
            if (satisfyingMoves != 0) {
                possibleMoves |= satisfyingMoves;
                moveTypes.putIfAbsent(MoveType.PAWN_ON_GO_ATTACK, satisfyingMoves);
            }
        }
        return moveTypes;
    }


}
