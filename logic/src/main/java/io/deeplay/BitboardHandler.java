package io.deeplay;

// TODO: implements MoveSystem
public class BitboardHandler {
    // TODO: конвертация ChessBoard в нужные битборды

    public static long getRookMoves(int from, long allPieces) {
        MagicBoard magic = BitboardPatternsInitializer.rookMagicBoards[from];
        return magic.moveBoards[(int) ((allPieces & magic.blockerMask) * BitboardPatternsInitializer.ROOK_MAGIC_NUMBERS[from] >>> magic.shift)];
    }

    public static long getBishopMoves(int from, long allPieces) {
        MagicBoard magic = BitboardPatternsInitializer.bishopMagicBoards[from];
        return magic.moveBoards[(int) ((allPieces & magic.blockerMask) * BitboardPatternsInitializer.BISHOP_MAGIC_NUMBERS[from] >>> magic.shift)];
    }

    public static long getQueenMoves(int from, long allPieces) {
        return getRookMoves(from, allPieces) | getBishopMoves(from, allPieces);
    }


}
