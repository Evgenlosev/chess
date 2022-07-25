package io.deeplay.api;

import io.deeplay.core.model.Side;
import io.deeplay.logic.BitUtils;
import io.deeplay.logic.BitboardDynamicPatterns;
import io.deeplay.logic.BitboardPatternsInitializer;
import io.deeplay.logic.ChessBoard;
import io.deeplay.model.*;
import io.deeplay.parser.FENParser;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// TODO: если наш ход и король противника под шахом (при том что он не зажат), такое невозможно либо исключение либо мат
// TODO: implements MoveSystem
// TODO: изменить логику определения фигур
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

    /**
     * Метод проверяет есть ли одинаковые биты
     * Пример
     * 0010 и 0110 -> true, т.к. маска 0010 верна для обоих
     * @param a
     * @param b
     * @return true если есть совпадающие биты
     */
    private static boolean containsSameBits(final long a, final long b) {
        return (a & b) != 0L;
    }

    public static Set<MoveInfo> getRookMoves(final io.deeplay.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getRooks(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getRooks(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        final long allPossibleMoves = getRookAllPossibleMovesBitboard(chessBitboard, from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_ROOK : Figure.B_ROOK;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getQueenMoves(final io.deeplay.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getQueens(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getQueens(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        final long allPossibleMoves =
                getRookAllPossibleMovesBitboard(chessBitboard, from)
                        | getBishopAllPossibleMovesBitboard(chessBitboard, from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_QUEEN : Figure.B_QUEEN;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getBishopMoves(final io.deeplay.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getBishops(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getBishops(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        final long allPossibleMoves = getBishopAllPossibleMovesBitboard(chessBitboard, from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_BISHOP : Figure.B_BISHOP;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getKnightMoves(final io.deeplay.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKnights(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKnights(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        final long allPossibleMoves = BitboardPatternsInitializer.knightMoveBitboards[from.getIndexAsOneDimension()];
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_KNIGHT : Figure.B_KNIGHT;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getKingMoves(final io.deeplay.logic.ChessBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        final long allPossibleMoves = BitboardPatternsInitializer.kingMoveBitboards[from.getIndexAsOneDimension()];
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_KING : Figure.B_KING;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getPawnMoves(final ChessBoard board, final Coord from) {
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

    // TODO: тест на связанность
    // TODO: тест на невозможность королю срубить фигуру
    // нужен королю, для проверки фигуры на связанность, для быстрого подсчета угрожающих фигур противника
    private long getOpponentAttacksBitboardFromAllFigures(final ChessBitboard chessBitboard) {
        long allAttacks = 0L;
        Map<MoveType, Long> pawnMoves;
        for (long pawn : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getPawns())) {
            pawnMoves = chessBitboard.getMySide() == Side.WHITE
                    ? BitboardDynamicPatterns.possibleBlackPawnMoves(chessBitboard, new Coord(Long.numberOfTrailingZeros(pawn)))
                    : BitboardDynamicPatterns.possibleWhitePawnMoves(chessBitboard, new Coord(Long.numberOfTrailingZeros(pawn)));
            for (long pawnMove : pawnMoves.values())
                allAttacks |= pawnMove;
        }
        for (long knight : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getRooks())) {
            allAttacks |= BitboardPatternsInitializer.knightMoveBitboards[Long.numberOfTrailingZeros(knight)];
        }
        for (long bishop : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getRooks())) {
            allAttacks |= getBishopAllPossibleMovesBitboard(chessBitboard, new Coord(Long.numberOfTrailingZeros(bishop)));
        }
        for (long rook : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getRooks())) {
            allAttacks |= getRookAllPossibleMovesBitboard(chessBitboard, new Coord(Long.numberOfTrailingZeros(rook)));
        }
        for (long queen : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getRooks())) {
            allAttacks |=
                    getBishopAllPossibleMovesBitboard(chessBitboard, new Coord(Long.numberOfTrailingZeros(queen)))
                            | getRookAllPossibleMovesBitboard(chessBitboard, new Coord(Long.numberOfTrailingZeros(queen)));
        }
        for (long king : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getRooks())) {
            allAttacks |= BitboardPatternsInitializer.kingMoveBitboards[Long.numberOfTrailingZeros(king)];
        }
        return allAttacks;
    }

    /*
    Multimap<Coord, MoveInfo> getAllPossibleMoves(ChessBoard board, Side side) {

    }
    */

    // TODO: long getAllPossibleMoves(ChessBoard board, Side side);
    // TODO: Multimap<Coord, MoveInfo> getAllPossibleMoves(ChessBoard board, Side side);
    // TODO: boolean isCheck(ChessBoard board, Side side);

}


