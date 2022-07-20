package io.deeplay;

import io.deeplay.core.model.Side;
import io.deeplay.logic.board.*;

import java.util.HashSet;
import java.util.Set;

// TODO: если наш ход и король противника под шахом (при том что он не зажат), такое невозможно либо исключение либо мат
// TODO: implements MoveSystem
public class BitboardHandler {
    public static Set<MoveInfo> getRookMoves(ChessBoard board, Coord from) {
        System.out.println("----------");
        ChessBitboard chessBitboard = new ChessBitboard(board.getFenNotation(), from.getIndexAsOneDimension());
        MagicBoard magic = BitboardPatternsInitializer.rookMagicBoards[from.getIndexAsOneDimension()];

        long allPossibleMoves = magic.moveBoards[(int) ((chessBitboard.getOccupied() & magic.blockerMask) *
                BitboardPatternsInitializer.ROOK_MAGIC_NUMBERS[from.getIndexAsOneDimension()] >>> magic.shift)];
        Set<MoveInfo> movesInfo = new HashSet<>();
        Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_ROOK : Figure.B_ROOK;

        System.out.println("occupied: ");
        BitUtils.printBitboardAsBinaryString(chessBitboard.getOccupied());
        System.out.println("my: ");
        BitUtils.printBitboardAsBinaryString(chessBitboard.getMyPieces());
        System.out.println("opponent: ");
        BitUtils.printBitboardAsBinaryString(chessBitboard.getOpponentPieces());

        long notMyPieces = ~chessBitboard.getMyPieces();
        long notOpponentPieces = ~chessBitboard.getOpponentPieces();
        for (long possibleMove : BitUtils.segregatePositions(allPossibleMoves)) {
            int debug = Long.numberOfTrailingZeros(possibleMove);
            if ((possibleMove & chessBitboard.getOpponentPieces()) != 0)
                movesInfo.add(new MoveInfo(from, new Coord(Long.numberOfTrailingZeros(possibleMove)),
                        MoveType.USUAL_ATTACK, figure));
            if ((possibleMove & notMyPieces & notOpponentPieces) != 0)
                movesInfo.add(new MoveInfo(from, new Coord(Long.numberOfTrailingZeros(possibleMove)),
                        MoveType.USUAL_MOVE, figure));
        }
        return movesInfo;
    }

    public static long getBishopMoves(int from, long allPieces) {
        MagicBoard magic = BitboardPatternsInitializer.bishopMagicBoards[from];
        return magic.moveBoards[(int) ((allPieces & magic.blockerMask) * BitboardPatternsInitializer.BISHOP_MAGIC_NUMBERS[from] >>> magic.shift)];
    }
/*

    public static long getQueenMoves(int from, long allPieces) {
        return getRookMoves(from, allPieces) | getBishopMoves(from, allPieces);
    }
*/
/*

    Set<MoveInfo> getKnightMoves(ChessBoard board, Coord from){

    }

    Set<MoveInfo> getKingMoves(ChessBoard board, Coord from){

    }
*/

    /*

    public interface MoveSystem {

    Set<MoveInfo> getPawnMoves(ChessBoard board, Coord from);

    Set<MoveInfo> getKnightMoves(ChessBoard board, Coord from);

    Set<MoveInfo> getBishopMoves(ChessBoard board, Coord from);

    Set<MoveInfo> getRookMoves(ChessBoard board, Coord from);

    Set<MoveInfo> getQueenMoves(ChessBoard board, Coord from);

    Set<MoveInfo> getKingMoves(ChessBoard board, Coord from);

    // TODO: Multimap<Coord, MoveInfo> getAllPossibleMoves(ChessBoard board, Side side);
    // TODO: boolean isCheck(ChessBoard board, Side side);

}

     */

}
