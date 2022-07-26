package io.deeplay.logic.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.deeplay.core.model.*;
import io.deeplay.logic.logic.BitUtils;
import io.deeplay.logic.logic.BitboardDynamicPatterns;
import io.deeplay.logic.logic.BitboardPatternsInitializer;
import io.deeplay.logic.model.ChessBitboard;
import io.deeplay.logic.model.MagicBoard;
import io.deeplay.logic.model.SideBitboards;
import io.deeplay.logic.parser.FENParser;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.deeplay.logic.logic.BitUtils.containsSameBits;


// TODO: если наш ход и король противника под шахом (при том что он не зажат), такое невозможно либо исключение либо мат
// TODO: implements MoveSystem
public class BitboardHandler {

    /**
     * Метод оборачивает все битборды в класс MoveInfo
     *
     * @param chessBitboard    текущее состояние доски
     * @param from             откуда ходим
     * @param allPossibleMoves маска всех возможных ходов фигуры
     * @param figure           сама фигура
     * @return множество всех возможных ходов
     */
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

    // этот метод позволяет атаковать своих(как и у слона), это полезно, для короля, чтобы он не смог срубить фигуру,
    // которая под линией атаки, учёт невозможности атаковать своих, связанность - считается отдельно
    private static long getRookAllPossibleMovesBitboard(final ChessBitboard chessBitboard, final Coord from) {
        final MagicBoard magic = BitboardPatternsInitializer.rookMagicBoards[from.getIndexAsOneDimension()];
        return magic.moveBoards[(int) ((chessBitboard.getOccupied() & magic.blockerMask) *
                BitboardPatternsInitializer.ROOK_MAGIC_NUMBERS[from.getIndexAsOneDimension()] >>> magic.shift)];
    }

    private static long getBishopAllPossibleMovesBitboard(final ChessBitboard chessBitboard, final Coord from) {
        final MagicBoard magic = BitboardPatternsInitializer.bishopMagicBoards[from.getIndexAsOneDimension()];
        return magic.moveBoards[(int) ((chessBitboard.getOccupied() & magic.blockerMask) *
                BitboardPatternsInitializer.BISHOP_MAGIC_NUMBERS[from.getIndexAsOneDimension()] >>> magic.shift)];
    }

    private static Set<MoveInfo> wrapRookMoves(final ChessBitboard chessBitboard, final Coord from) {
        final long allPossibleMoves = getRookAllPossibleMovesBitboard(chessBitboard, from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_ROOK : Figure.B_ROOK;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getRookMoves(final io.deeplay.logic.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getRooks(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getRooks(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        return wrapRookMoves(chessBitboard, from);
    }

    private static Set<MoveInfo> wrapQueenMoves(final ChessBitboard chessBitboard, final Coord from) {
        final long allPossibleMoves =
                getRookAllPossibleMovesBitboard(chessBitboard, from)
                        | getBishopAllPossibleMovesBitboard(chessBitboard, from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_QUEEN : Figure.B_QUEEN;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getQueenMoves(final io.deeplay.logic.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getQueens(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getQueens(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        return wrapQueenMoves(chessBitboard, from);
    }

    private static Set<MoveInfo> wrapBishopMoves(final ChessBitboard chessBitboard, final Coord from) {
        final long allPossibleMoves = getBishopAllPossibleMovesBitboard(chessBitboard, from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_BISHOP : Figure.B_BISHOP;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getBishopMoves(final io.deeplay.logic.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getBishops(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getBishops(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        return wrapBishopMoves(chessBitboard, from);
    }

    private static Set<MoveInfo> wrapKnightMoves(final ChessBitboard chessBitboard, final Coord from) {
        final long allPossibleMoves = BitboardPatternsInitializer.knightMoveBitboards[from.getIndexAsOneDimension()];
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_KNIGHT : Figure.B_KNIGHT;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getKnightMoves(final io.deeplay.logic.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKnights(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKnights(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        return wrapKnightMoves(chessBitboard, from);
    }

    private static Set<MoveInfo> wrapKingMoves(final ChessBitboard chessBitboard, final Coord from) {
        final long allPossibleMoves = BitboardPatternsInitializer.kingMoveBitboards[from.getIndexAsOneDimension()];
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_KING : Figure.B_KING;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getKingMoves(final io.deeplay.logic.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        return wrapKingMoves(chessBitboard, from);
    }

    private static Set<MoveInfo> wrapPawnMoves(final ChessBitboard chessBitboard, final Coord from) {
        final Map<MoveType, Long> allPossibleMoves = chessBitboard.getMySide() == Side.WHITE
                ? BitboardDynamicPatterns.possibleWhitePawnMoves(chessBitboard, from)
                : BitboardDynamicPatterns.possibleBlackPawnMoves(chessBitboard, from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_PAWN : Figure.B_PAWN;
        Set<MoveInfo> movesInfo = new HashSet<>();

        for (MoveType moveType : allPossibleMoves.keySet()) {
            for (long possibleMove : BitUtils.segregatePositions(allPossibleMoves.get(moveType))) {
                if (possibleMove != 0L)
                    movesInfo.add(new MoveInfo(from, new Coord(Long.numberOfTrailingZeros(possibleMove)),
                            moveType, figure));
            }
        }
        return movesInfo;
    }

    public static Set<MoveInfo> getPawnMoves(final io.deeplay.logic.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getPawns(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getPawns(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        chessBitboard.setEnPassantFile(FENParser.getEnPassant(board.getFenNotation()));

        return wrapPawnMoves(chessBitboard, from);
    }

    public static Multimap<Coord, MoveInfo> getAllPossibleMoves(final io.deeplay.logic.logic.ChessBoard board, final Side side) {
        Multimap<Coord, MoveInfo> allPossibleMoves = HashMultimap.create();
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());
        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (side == Side.WHITE)
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (side == Side.BLACK)
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Сторона не определена");

        int index;
        for (long pawn : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getPawns())) {
            index = Long.numberOfTrailingZeros(pawn);
            allPossibleMoves.putAll(new Coord(index), wrapPawnMoves(chessBitboard, new Coord(index)));
        }
        if (chessBitboard.getMyBitboards().getKnights() != 0L)
            for (long knight : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getKnights())) {
                index = Long.numberOfTrailingZeros(knight);
                allPossibleMoves.putAll(new Coord(index), wrapKnightMoves(chessBitboard, new Coord(index)));
            }
        if (chessBitboard.getMyBitboards().getBishops() != 0L)
            for (long bishop : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getBishops())) {
                index = Long.numberOfTrailingZeros(bishop);
                allPossibleMoves.putAll(new Coord(index), wrapBishopMoves(chessBitboard, new Coord(index)));
            }
        if (chessBitboard.getMyBitboards().getRooks() != 0L)
            for (long rook : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getRooks())) {
                index = Long.numberOfTrailingZeros(rook);
                allPossibleMoves.putAll(new Coord(index), wrapRookMoves(chessBitboard, new Coord(index)));
            }
        if (chessBitboard.getMyBitboards().getQueens() != 0L)
            for (long queen : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getQueens())) {
                index = Long.numberOfTrailingZeros(queen);
                allPossibleMoves.putAll(new Coord(index), wrapQueenMoves(chessBitboard, new Coord(index)));
            }
        for (long king : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getKing())) {
            // короля не может не быть на доске
            index = Long.numberOfTrailingZeros(king);
            allPossibleMoves.putAll(new Coord(index), wrapKingMoves(chessBitboard, new Coord(index)));
        }
        return allPossibleMoves;
    }

    // нужен королю, для быстрого подсчета угрожающих фигур противника
    static long getOpponentAttacksBitboardFromAllFigures(final ChessBitboard chessBitboard) {
        long allAttacks = 0L;
        for (long pawn : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getPawns())) {
            // инвертированы методы, т.к. атаки нужно именно противника (другой стороны)
            allAttacks |= chessBitboard.getMySide() == Side.WHITE
                    ? BitboardDynamicPatterns.possibleBlackPawnAttacks
                    (new Coord(Long.numberOfTrailingZeros(pawn)))
                    : BitboardDynamicPatterns.possibleWhitePawnAttacks
                    (new Coord(Long.numberOfTrailingZeros(pawn)));
        }
        if (chessBitboard.getOpponentBitboards().getKnights() != 0L)
            for (long knight : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getKnights())) {
                allAttacks |= BitboardPatternsInitializer.knightMoveBitboards[Long.numberOfTrailingZeros(knight)];
            }
        if (chessBitboard.getOpponentBitboards().getBishops() != 0L)
            for (long bishop : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getBishops())) {
                allAttacks |=
                        getBishopAllPossibleMovesBitboard(chessBitboard, new Coord(Long.numberOfTrailingZeros(bishop)));
            }
        if (chessBitboard.getOpponentBitboards().getRooks() != 0L)
            for (long rook : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getRooks())) {
                allAttacks |= getRookAllPossibleMovesBitboard
                        (chessBitboard, new Coord(Long.numberOfTrailingZeros(rook)));
            }
        if (chessBitboard.getOpponentBitboards().getQueens() != 0L)
            for (long queen : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getQueens())) {
                allAttacks |=
                        getBishopAllPossibleMovesBitboard
                                (chessBitboard, new Coord(Long.numberOfTrailingZeros(queen)))
                                | getRookAllPossibleMovesBitboard
                                (chessBitboard, new Coord(Long.numberOfTrailingZeros(queen)));
            }
        for (long king : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getKing())) {
            // короля не может не быть на доске
            allAttacks |= BitboardPatternsInitializer.kingMoveBitboards[Long.numberOfTrailingZeros(king)];
        }
        return allAttacks;
    }


    // TODO: Multimap<Coord, MoveInfo> getAllPossibleMoves(io.deeplay.logic.logic.ChessBoard board, Side side);
    // TODO: boolean isCheck(io.deeplay.logic.logic.ChessBoard board, Side side);

}


