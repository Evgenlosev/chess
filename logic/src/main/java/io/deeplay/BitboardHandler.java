package io.deeplay;

import io.deeplay.core.model.Side;
import io.deeplay.logic.board.*;

import java.util.HashSet;
import java.util.Set;

// TODO: если наш ход и король противника под шахом (при том что он не зажат), такое невозможно либо исключение либо мат
// TODO: implements MoveSystem
public class BitboardHandler {
    public static Set<MoveInfo> getRookMoves(ChessBoard board, Coord from) {
        ChessBitboard chessBitboard = new ChessBitboard(board.getFenNotation(), from.getIndexAsOneDimension());
        MagicBoard magic = BitboardPatternsInitializer.rookMagicBoards[from.getIndexAsOneDimension()];

        long allPossibleMoves = magic.moveBoards[(int) ((chessBitboard.getOccupied() & magic.blockerMask) *
                BitboardPatternsInitializer.ROOK_MAGIC_NUMBERS[from.getIndexAsOneDimension()] >>> magic.shift)];
        Set<MoveInfo> movesInfo = new HashSet<>();
        Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_ROOK : Figure.B_ROOK;

        long notMyPieces = ~chessBitboard.getMyPieces();
        long notOpponentPieces = ~chessBitboard.getOpponentPieces();
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

    Set<MoveInfo> getKnightMoves(ChessBoard board, Coord from){
        ChessBitboard chessBitboard = new ChessBitboard(board.getFenNotation(), from.getIndexAsOneDimension());

        long allPossibleMoves = BitboardPatternsInitializer.knightMoveBitboards[from.getIndexAsOneDimension()];
        Set<MoveInfo> movesInfo = new HashSet<>();
        Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_KNIGHT : Figure.B_KNIGHT;

        long notMyPieces = ~chessBitboard.getMyPieces();
        long notOpponentPieces = ~chessBitboard.getOpponentPieces();
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

    Set<MoveInfo> getKingMoves(ChessBoard board, Coord from){
        ChessBitboard chessBitboard = new ChessBitboard(board.getFenNotation(), from.getIndexAsOneDimension());

        long allPossibleMoves = BitboardPatternsInitializer.kingMoveBitboards[from.getIndexAsOneDimension()];
        Set<MoveInfo> movesInfo = new HashSet<>();
        Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_KING : Figure.B_KING;

        long notMyPieces = ~chessBitboard.getMyPieces();
        long notOpponentPieces = ~chessBitboard.getOpponentPieces();
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


/*
// TODO:
    public static long getBishopMoves(int from, long allPieces)
    public static long getQueenMoves(int from, long allPieces)
*/
    // TODO: Multimap<Coord, MoveInfo> getAllPossibleMoves(ChessBoard board, Side side);
    // TODO: boolean isCheck(ChessBoard board, Side side);

}


