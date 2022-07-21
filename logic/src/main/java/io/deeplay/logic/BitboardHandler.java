package io.deeplay.logic;

import io.deeplay.core.model.Side;
import io.deeplay.logic.board.*;

import java.util.HashSet;
import java.util.Set;

// TODO: если наш ход и король противника под шахом (при том что он не зажат), такое невозможно либо исключение либо мат
// TODO: implements MoveSystem
// TODO: изменить логику определения фигур
public class BitboardHandler {

    private static Set<MoveInfo> wrapUpMoves(final ChessBitboard chessBitboard,
                                             final Coord from,
                                             final long allPossibleMoves,
                                             final Figure figure) {
        Set<MoveInfo> movesInfo = new HashSet<>();
        final long notMyPieces = ~chessBitboard.getMyPieces();
        final long notOpponentPieces = ~chessBitboard.getOpponentPieces();
        for (long possibleMove : BitUtils.segregatePositions(allPossibleMoves)) {
            if ((possibleMove & chessBitboard.getOpponentPieces()) != 0)
                movesInfo.add(new MoveInfo(from, new Coord(Long.numberOfTrailingZeros(possibleMove)),
                        MoveType.USUAL_ATTACK, figure));
            if ((possibleMove & notMyPieces & notOpponentPieces) != 0)
                movesInfo.add(new MoveInfo(from, new Coord(Long.numberOfTrailingZeros(possibleMove)),
                        MoveType.USUAL_MOVE, figure));
        }
        return movesInfo;
    }

    private static long getRookPossibleMoves(final ChessBitboard chessBitboard, final Coord from) {
        MagicBoard magic = BitboardPatternsInitializer.rookMagicBoards[from.getIndexAsOneDimension()];
        return magic.moveBoards[(int) ((chessBitboard.getOccupied() & magic.blockerMask) *
                BitboardPatternsInitializer.ROOK_MAGIC_NUMBERS[from.getIndexAsOneDimension()] >>> magic.shift)];
    }

    private static long getBishopPossibleMoves(final ChessBitboard chessBitboard, final Coord from) {
        MagicBoard magic = BitboardPatternsInitializer.bishopMagicBoards[from.getIndexAsOneDimension()];
        return magic.moveBoards[(int) ((chessBitboard.getOccupied() & magic.blockerMask) *
                BitboardPatternsInitializer.BISHOP_MAGIC_NUMBERS[from.getIndexAsOneDimension()] >>> magic.shift)];
    }

    public static Set<MoveInfo> getRookMoves(final ChessBoard board, final Coord from) {
        ChessBitboard chessBitboard = new ChessBitboard(board.getFenNotation(), from.getIndexAsOneDimension());

        final long allPossibleMoves = getRookPossibleMoves(chessBitboard, from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_ROOK : Figure.B_ROOK;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getQueenMoves(final ChessBoard board, final Coord from) {
        ChessBitboard chessBitboard = new ChessBitboard(board.getFenNotation(), from.getIndexAsOneDimension());

        final long allPossibleMoves =
                getRookPossibleMoves(chessBitboard, from) | getBishopPossibleMoves(chessBitboard, from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_QUEEN : Figure.B_QUEEN;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getBishopMoves(final ChessBoard board, final Coord from) {
        ChessBitboard chessBitboard = new ChessBitboard(board.getFenNotation(), from.getIndexAsOneDimension());

        final long allPossibleMoves = getBishopPossibleMoves(chessBitboard, from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_BISHOP : Figure.B_BISHOP;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    Set<MoveInfo> getKnightMoves(final ChessBoard board, final Coord from) {
        ChessBitboard chessBitboard = new ChessBitboard(board.getFenNotation(), from.getIndexAsOneDimension());

        final long allPossibleMoves = BitboardPatternsInitializer.knightMoveBitboards[from.getIndexAsOneDimension()];
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_KNIGHT : Figure.B_KNIGHT;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    Set<MoveInfo> getKingMoves(final ChessBoard board, final Coord from) {
        ChessBitboard chessBitboard = new ChessBitboard(board.getFenNotation(), from.getIndexAsOneDimension());

        final long allPossibleMoves = BitboardPatternsInitializer.kingMoveBitboards[from.getIndexAsOneDimension()];
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_KING : Figure.B_KING;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }


/*
// TODO:
    public static long getPawnMoves(int from, long allPieces)
*/
    // TODO: Multimap<Coord, MoveInfo> getAllPossibleMoves(ChessBoard board, Side side);
    // TODO: boolean isCheck(ChessBoard board, Side side);

}


