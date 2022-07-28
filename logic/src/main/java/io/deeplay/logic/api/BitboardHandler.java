package io.deeplay.logic.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.deeplay.core.model.*;
import io.deeplay.logic.logic.BitUtils;
import io.deeplay.logic.logic.BitboardDynamicPatterns;
import io.deeplay.logic.logic.BitboardPatternsInitializer;
import io.deeplay.logic.logic.FENBoard;
import io.deeplay.logic.model.*;
import io.deeplay.logic.parser.FENParser;

import java.util.*;

import static io.deeplay.logic.logic.BitUtils.containsSameBits;
import static io.deeplay.logic.logic.BitUtils.inBetween;


/**
 * Класс который, принимает нотацию FEN для того, чтобы передать возможные ходы.
 * Если подать на вход FEN с законченной игрой (мат/пат),
 * неверной логикой (нам шах(при чём мата/пата нет), а ход противника), то выбросится исключение.
 * За подобными нарушениями следит ContractHandler.
 * За нарушениями сигнатуры нотации FEN - следит FENParser.
 * Данный класс сам уведомит о мате/пате противнику, с помощью MoveInfo.
 */
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
    private static long getRookAllPossibleMovesBitboard(final long occupied, final Coord from) {
        final MagicBoard magic = BitboardPatternsInitializer.rookMagicBoards[from.getIndexAsOneDimension()];
        return magic.moveBoards[(int) ((occupied & magic.blockerMask) *
                BitboardPatternsInitializer.ROOK_MAGIC_NUMBERS[from.getIndexAsOneDimension()] >>> magic.shift)];
    }

    private static long getBishopAllPossibleMovesBitboard(final long occupied, final Coord from) {
        final MagicBoard magic = BitboardPatternsInitializer.bishopMagicBoards[from.getIndexAsOneDimension()];
        return magic.moveBoards[(int) ((occupied & magic.blockerMask) *
                BitboardPatternsInitializer.BISHOP_MAGIC_NUMBERS[from.getIndexAsOneDimension()] >>> magic.shift)];
    }

    private static Set<MoveInfo> wrapRookMoves(final ChessBitboard chessBitboard, final Coord from) {
        final long allPossibleMoves = getRookAllPossibleMovesBitboard(chessBitboard.getOccupied(), from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_ROOK : Figure.B_ROOK;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getRookMoves(final FENBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getRooks(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getRooks(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        Objects.requireNonNull(chessBitboard);

        return wrapRookMoves(chessBitboard, from);
    }

    private static Set<MoveInfo> wrapQueenMoves(final ChessBitboard chessBitboard, final Coord from) {
        final long allPossibleMoves =
                getQueenAllPossibleMovesBitboard(chessBitboard.getOccupied(), from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_QUEEN : Figure.B_QUEEN;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getQueenMoves(final FENBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getQueens(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getQueens(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        Objects.requireNonNull(chessBitboard);

        return wrapQueenMoves(chessBitboard, from);
    }

    private static Set<MoveInfo> wrapBishopMoves(final ChessBitboard chessBitboard, final Coord from) {
        final long allPossibleMoves = getBishopAllPossibleMovesBitboard(chessBitboard.getOccupied(), from);
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_BISHOP : Figure.B_BISHOP;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getBishopMoves(final FENBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getBishops(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getBishops(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        Objects.requireNonNull(chessBitboard);

        return wrapBishopMoves(chessBitboard, from);
    }

    private static Set<MoveInfo> wrapKnightMoves(final ChessBitboard chessBitboard, final Coord from) {
        final long allPossibleMoves = BitboardPatternsInitializer.knightMoveBitboards[from.getIndexAsOneDimension()];
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_KNIGHT : Figure.B_KNIGHT;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getKnightMoves(final FENBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKnights(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKnights(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        Objects.requireNonNull(chessBitboard);

        return wrapKnightMoves(chessBitboard, from);
    }

    private static Set<MoveInfo> wrapKingMoves(final ChessBitboard chessBitboard, final Coord from) {
        final long allPossibleMoves = BitboardPatternsInitializer.kingMoveBitboards[from.getIndexAsOneDimension()];
        final Figure figure = chessBitboard.getMySide() == Side.WHITE ? Figure.W_KING : Figure.B_KING;

        return wrapUpMoves(chessBitboard, from, allPossibleMoves, figure);
    }

    public static Set<MoveInfo> getKingMoves(final FENBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        Objects.requireNonNull(chessBitboard);

        return wrapKingMoves(chessBitboard, from);
    }

    private static Set<MoveInfo> wrapPawnMoves(final ChessBitboard chessBitboard, final Coord from) {
        final Map<MoveType, Long> allPossibleMoves = getSidePawnMoves(chessBitboard, from);

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

    public static Set<MoveInfo> getPawnMoves(final FENBoard board, final Coord from) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getPawns(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getPawns(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        Objects.requireNonNull(chessBitboard);

        chessBitboard.setEnPassantFile(FENParser.getEnPassant(board.getFenNotation()));

        return wrapPawnMoves(chessBitboard, from);
    }

    public static Multimap<Coord, MoveInfo> getAllPossibleMoves(final FENBoard board, final Side side) {
        Objects.requireNonNull(side);
        Multimap<Coord, MoveInfo> allPossibleMoves = HashMultimap.create();
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(board.getFenNotation());
        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (side == Side.WHITE)
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (side == Side.BLACK)
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));

        int index;
        for (long pawn : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getPawns())) {
            index = Long.numberOfTrailingZeros(pawn);
            allPossibleMoves.putAll(new Coord(index), wrapPawnMoves(chessBitboard, new Coord(index)));
        }
        for (long knight : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getKnights())) {
            index = Long.numberOfTrailingZeros(knight);
            allPossibleMoves.putAll(new Coord(index), wrapKnightMoves(chessBitboard, new Coord(index)));
        }
        for (long bishop : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getBishops())) {
            index = Long.numberOfTrailingZeros(bishop);
            allPossibleMoves.putAll(new Coord(index), wrapBishopMoves(chessBitboard, new Coord(index)));
        }
        for (long rook : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getRooks())) {
            index = Long.numberOfTrailingZeros(rook);
            allPossibleMoves.putAll(new Coord(index), wrapRookMoves(chessBitboard, new Coord(index)));
        }
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

    static long getSideAttacksBitboardFromAllFigures(final ChessBitboard chessBitboard) {
        long allAttacks = 0L;
        for (long pawn : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getPawns()))
            allAttacks |= getOpponentPawnAttacksBitboard(chessBitboard, pawn);

        for (long knight : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getKnights()))
            allAttacks |= BitboardPatternsInitializer.knightMoveBitboards[Long.numberOfTrailingZeros(knight)];

        for (long bishop : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getBishops()))
            allAttacks |=
                    getBishopAllPossibleMovesBitboard(chessBitboard.getOccupied(),
                            new Coord(Long.numberOfTrailingZeros(bishop)));

        for (long rook : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getRooks()))
            allAttacks |= getRookAllPossibleMovesBitboard
                    (chessBitboard.getOccupied(), new Coord(Long.numberOfTrailingZeros(rook)));

        for (long queen : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getQueens()))
            allAttacks |= getQueenAllPossibleMovesBitboard(chessBitboard.getOccupied(),
                    new Coord(Long.numberOfTrailingZeros(queen)));

        for (long king : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getKing())) {
            // короля не может не быть на доске
            allAttacks |= BitboardPatternsInitializer.kingMoveBitboards[Long.numberOfTrailingZeros(king)];
        }
        return allAttacks;
    }

    // нужно знать количество шахующих(чтобы фигуры кроме короля знали, что им нельзя двигаться) -
    // отдельный метод который при первой встрече второй угрожающей фигуры возвращает true

    // TODO: надо считать что король ПРОТИВНИКА под шахом/матом/ничья, чтобы заполнить MoveInfo
    // метод атаки на текущую клетку (учитываются СВОИ фигуры)
    // если при паттерне коня при пробеге по коням противника нашлось совпадение то добавляем в битборд клетки под атакой
    // нужно вернуть клетки под атакой


    private static Map<MoveType, Long> getSidePawnMoves(final ChessBitboard chessBitboard, final Coord from) {
        if (chessBitboard.getMySide() == Side.WHITE)
            return BitboardDynamicPatterns.possibleWhitePawnMoves(chessBitboard, from);
        else if (chessBitboard.getMySide() == Side.BLACK)
            return BitboardDynamicPatterns.possibleBlackPawnMoves(chessBitboard, from);
        return Collections.emptyMap();
    }

    private static long getSidePawnMovesBitboard(final ChessBitboard chessBitboard, final long pawnBitboard) {
        if (chessBitboard.getMySide() == Side.WHITE)
            return BitboardDynamicPatterns.possibleWhitePawnMovesBitboard(chessBitboard,
                    new Coord(Long.numberOfTrailingZeros(pawnBitboard)));
        else if (chessBitboard.getMySide() == Side.BLACK)
            return BitboardDynamicPatterns.possibleBlackPawnMovesBitboard(chessBitboard,
                    new Coord(Long.numberOfTrailingZeros(pawnBitboard)));
        return 0L;
    }

    private static long getOpponentPawnAttacksBitboard(final ChessBitboard chessBitboard, final long pawnBitboard) {
        // инвертированы методы, т.к. атаки нужно именно противника (другой стороны)
        if (chessBitboard.getMySide() == Side.WHITE)
            return BitboardDynamicPatterns
                    .possibleBlackPawnAttacks(new Coord(Long.numberOfTrailingZeros(pawnBitboard)));
        else if (chessBitboard.getMySide() == Side.BLACK)
            return BitboardDynamicPatterns
                    .possibleWhitePawnAttacks(new Coord(Long.numberOfTrailingZeros(pawnBitboard)));
        return 0L;
    }

    /*
    public boolean isCheck(final FENBoard board, final Side side) {

    }
*/
    private static long getQueenAllPossibleMovesBitboard(final long occupied, final Coord from) {
        return getBishopAllPossibleMovesBitboard(occupied, from)
                | getRookAllPossibleMovesBitboard(occupied, from);
    }

    public static CheckData getCheckData(final ChessBitboard chessBitboard) {
        final long myKingBitboard = chessBitboard.getMyBitboards().getKing();
        final int kingIndex = Long.numberOfTrailingZeros(myKingBitboard);
        int countChecks = 0;
        long pieceMoves;
        long allAttacks = 0L;
        long threateningPiecePositionBitboard = 0L;
        // тут учитывается, позиция короля под атакой (главное не срубить короля случайно)
        long threatPieceBitboard = 0L;
        int opponentPieceIndex;
        for (long queen : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getQueens())) {
            opponentPieceIndex = Long.numberOfTrailingZeros(queen);
            pieceMoves =
                    getQueenAllPossibleMovesBitboard
                            (chessBitboard.getOccupied(),
                                    new Coord(opponentPieceIndex));
            if (containsSameBits(pieceMoves, myKingBitboard)) {
                countChecks++;
                threatPieceBitboard |= queen | (pieceMoves & inBetween(kingIndex, opponentPieceIndex));
                threateningPiecePositionBitboard |= queen;
            }
            // т.к. король не сможет двигаться по всей линии,
            // нужно просчитать ее всю, чтобы король не смог отступить назад
            allAttacks |= getQueenAllPossibleMovesBitboard
                    (chessBitboard.getOccupied() ^ myKingBitboard,
                            new Coord(opponentPieceIndex));
        }
        for (long knight : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getKnights())) {
            pieceMoves = BitboardPatternsInitializer.knightMoveBitboards[Long.numberOfTrailingZeros(knight)];
            if (containsSameBits(pieceMoves, myKingBitboard)) {
                countChecks++;
                // короля добавляем, только чтобы убрать в конце метода
                threatPieceBitboard |= knight | myKingBitboard; // от коня можно защитить только атакой
                threateningPiecePositionBitboard |= knight;
            }
            allAttacks |= pieceMoves;
        }
        for (long bishop : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getBishops())) {
            opponentPieceIndex = Long.numberOfTrailingZeros(bishop);
            pieceMoves =
                    getBishopAllPossibleMovesBitboard(chessBitboard.getOccupied(),
                            new Coord(opponentPieceIndex));
            if (containsSameBits(pieceMoves, myKingBitboard)) {
                countChecks++;
                threatPieceBitboard |= bishop | (pieceMoves & inBetween(kingIndex, opponentPieceIndex));
                threateningPiecePositionBitboard |= bishop;
            }
            allAttacks |= getBishopAllPossibleMovesBitboard(chessBitboard.getOccupied() ^ myKingBitboard,
                    new Coord(opponentPieceIndex));
        }
        for (long rook : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getRooks())) {
            opponentPieceIndex = Long.numberOfTrailingZeros(rook);
            pieceMoves = getRookAllPossibleMovesBitboard(chessBitboard.getOccupied(),
                    new Coord(opponentPieceIndex));
            if (containsSameBits(pieceMoves, myKingBitboard)) {
                countChecks++;
                threatPieceBitboard |= rook | (pieceMoves & inBetween(kingIndex, opponentPieceIndex));
                threateningPiecePositionBitboard |= rook;
            }
            allAttacks |= getRookAllPossibleMovesBitboard(chessBitboard.getOccupied() ^ myKingBitboard,
                    new Coord(opponentPieceIndex));
        }
        for (long pawn : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getPawns())) {
            pieceMoves = getOpponentPawnAttacksBitboard(chessBitboard, pawn);
            if (containsSameBits(pieceMoves, myKingBitboard)) {
                countChecks++;
                threatPieceBitboard |= pawn | myKingBitboard; // от пешки можно защитить только атакой
                threateningPiecePositionBitboard |= pawn;
            }
            allAttacks |= pieceMoves;
        }
        if (countChecks > 1)
            return new CheckData(CheckType.TWO, 0L, 0L, allAttacks);
        if (countChecks == 1)
            return new CheckData(CheckType.ONE,
                    threatPieceBitboard ^ myKingBitboard,
                    threateningPiecePositionBitboard,
                    allAttacks);

        return new CheckData(CheckType.NONE, 0L, 0L, allAttacks);
    }

    public static Map<Integer, Long> getAllPossibleMoves(final ChessBitboard chessBitboard,
                                                         final CheckData checkData) {
        Map<Integer, Long> allPossibleMoves = new HashMap<>();
        final long myKing = chessBitboard.getMyBitboards().getKing();
        final int kingIndex = Long.numberOfTrailingZeros(myKing);
        allPossibleMoves.put(kingIndex, BitboardPatternsInitializer.kingMoveBitboards[kingIndex]
                & ~checkData.getAllAttacks() & ~chessBitboard.getMyPieces());
        final long occupied = chessBitboard.getOccupied();
        final long notMyPieces = ~chessBitboard.getMyPieces();
        long opponentPieceAttack;
        long threatPieceBitboard = checkData.getThreatPieceBitboard(); // копия
        if (threatPieceBitboard == 0L)
            threatPieceBitboard = ~0L;

        // считаем связанность
        // стоит воспринимать каждую фигуру как стенку, каждую из которых мы по очереди снимаем
        for (long bishop : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getBishops())) {
            // для фигуры которая поставила шах связанность считать не надо
            if (bishop == checkData.getThreateningPiecePositionBitboard())
                continue;
            for (long myPawn : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getPawns())) {
                // смотрим атаку противника без учёта текущей нашей фигуры
                opponentPieceAttack =
                        getBishopAllPossibleMovesBitboard(occupied ^ myPawn,
                                new Coord(Long.numberOfTrailingZeros(bishop)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    // объединяем атаку связывающей фигуры и возможности походить нашей
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myPawn),
                            getSidePawnMovesBitboard(chessBitboard, myPawn)
                                    & opponentPieceAttack & threatPieceBitboard);
                // объединяем множества ходов связывающей фигуры без учета связанной,
                // и ходы связанной фигуры (& opponentPieceAttack)
                // если шах, то не должны учитываться другие способы ходить => & threatPieceBitboard
            }
            for (long myKnight : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getKnights())) {
                opponentPieceAttack =
                        getBishopAllPossibleMovesBitboard(occupied ^ myKnight,
                                new Coord(Long.numberOfTrailingZeros(bishop)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myKnight),
                            BitboardPatternsInitializer.knightMoveBitboards[Long.numberOfTrailingZeros(myKnight)]
                                    & opponentPieceAttack & threatPieceBitboard);
            }
            for (long myBishop : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getBishops())) {
                opponentPieceAttack =
                        getBishopAllPossibleMovesBitboard(occupied ^ myBishop,
                                new Coord(Long.numberOfTrailingZeros(bishop)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myBishop),
                            getBishopAllPossibleMovesBitboard(occupied,
                                    new Coord(Long.numberOfTrailingZeros(myBishop)))
                                    & opponentPieceAttack & threatPieceBitboard);
            }
            for (long myRook : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getRooks())) {
                opponentPieceAttack =
                        getBishopAllPossibleMovesBitboard(occupied ^ myRook,
                                new Coord(Long.numberOfTrailingZeros(bishop)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myRook),
                            getRookAllPossibleMovesBitboard(occupied,
                                    new Coord(Long.numberOfTrailingZeros(myRook)))
                                    & opponentPieceAttack & threatPieceBitboard);
            }
            for (long myQueen : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getQueens())) {
                opponentPieceAttack =
                        getBishopAllPossibleMovesBitboard(occupied ^ myQueen,
                                new Coord(Long.numberOfTrailingZeros(bishop)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myQueen),
                            getQueenAllPossibleMovesBitboard(occupied,
                                    new Coord(Long.numberOfTrailingZeros(myQueen)))
                                    & opponentPieceAttack & threatPieceBitboard);
            }
        }
        for (long rook : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getRooks())) {
            if (rook == checkData.getThreateningPiecePositionBitboard())
                continue;
            for (long myPawn : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getPawns())) {
                opponentPieceAttack =
                        getRookAllPossibleMovesBitboard(occupied ^ myPawn,
                                new Coord(Long.numberOfTrailingZeros(rook)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    // объединяем атаку связывающей фигуры и возможности походить нашей
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myPawn),
                            getSidePawnMovesBitboard(chessBitboard, myPawn)
                                    & opponentPieceAttack & threatPieceBitboard);
            }
            for (long myKnight : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getKnights())) {
                opponentPieceAttack =
                        getRookAllPossibleMovesBitboard(occupied ^ myKnight,
                                new Coord(Long.numberOfTrailingZeros(rook)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myKnight),
                            BitboardPatternsInitializer.knightMoveBitboards[Long.numberOfTrailingZeros(myKnight)]
                                    & opponentPieceAttack & threatPieceBitboard);
            }
            for (long myBishop : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getBishops())) {
                opponentPieceAttack =
                        getRookAllPossibleMovesBitboard(occupied ^ myBishop,
                                new Coord(Long.numberOfTrailingZeros(rook)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myBishop),
                            getBishopAllPossibleMovesBitboard(occupied,
                                    new Coord(Long.numberOfTrailingZeros(myBishop)))
                                    & opponentPieceAttack & threatPieceBitboard); // может нападать на своих
            }
            for (long myRook : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getRooks())) {
                opponentPieceAttack =
                        getRookAllPossibleMovesBitboard(occupied ^ myRook,
                                new Coord(Long.numberOfTrailingZeros(rook)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myRook),
                            getRookAllPossibleMovesBitboard(occupied,
                                    new Coord(Long.numberOfTrailingZeros(myRook)))
                                    & opponentPieceAttack & threatPieceBitboard);
            }
            for (long myQueen : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getQueens())) {
                opponentPieceAttack =
                        getRookAllPossibleMovesBitboard(occupied ^ myQueen,
                                new Coord(Long.numberOfTrailingZeros(rook)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myQueen),
                            getQueenAllPossibleMovesBitboard(occupied,
                                    new Coord(Long.numberOfTrailingZeros(myQueen)))
                                    & opponentPieceAttack & threatPieceBitboard);
            }
        }

        for (long queen : BitUtils.segregatePositions(chessBitboard.getOpponentBitboards().getQueens())) {
            if (queen == checkData.getThreateningPiecePositionBitboard())
                continue;
            for (long myPawn : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getPawns())) {
                opponentPieceAttack =
                        getQueenAllPossibleMovesBitboard(occupied ^ myPawn,
                                new Coord(Long.numberOfTrailingZeros(queen)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    // объединяем атаку связывающей фигуры и возможности походить нашей
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myPawn),
                            getSidePawnMovesBitboard(chessBitboard, myPawn)
                                    & opponentPieceAttack & threatPieceBitboard);
            }
            for (long myKnight : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getKnights())) {
                opponentPieceAttack =
                        getQueenAllPossibleMovesBitboard(occupied ^ myKnight,
                                new Coord(Long.numberOfTrailingZeros(queen)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myKnight),
                            BitboardPatternsInitializer.knightMoveBitboards[Long.numberOfTrailingZeros(myKnight)]
                                    & opponentPieceAttack & threatPieceBitboard);
            }
            for (long myBishop : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getBishops())) {
                opponentPieceAttack =
                        getQueenAllPossibleMovesBitboard(occupied ^ myBishop,
                                new Coord(Long.numberOfTrailingZeros(queen)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myBishop),
                            getBishopAllPossibleMovesBitboard(occupied,
                                    new Coord(Long.numberOfTrailingZeros(myBishop)))
                                    & opponentPieceAttack & threatPieceBitboard);
            }
            for (long myRook : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getRooks())) {
                opponentPieceAttack =
                        getQueenAllPossibleMovesBitboard(occupied ^ myRook,
                                new Coord(Long.numberOfTrailingZeros(queen)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myRook),
                            getRookAllPossibleMovesBitboard(occupied,
                                    new Coord(Long.numberOfTrailingZeros(myRook)))
                                    & opponentPieceAttack & threatPieceBitboard);
            }
            for (long myQueen : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getQueens())) {
                opponentPieceAttack =
                        getQueenAllPossibleMovesBitboard(occupied ^ myQueen,
                                new Coord(Long.numberOfTrailingZeros(queen)));
                if (containsSameBits(opponentPieceAttack, myKing))
                    allPossibleMoves.put(Long.numberOfTrailingZeros(myQueen),
                            getQueenAllPossibleMovesBitboard(occupied,
                                    new Coord(Long.numberOfTrailingZeros(myQueen)))
                                    & opponentPieceAttack & threatPieceBitboard);
            }
        }
        // исключая позиции которые есть в мапе надо посчитать ходы
        for (long myPawn : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getPawns())) {
            if (!allPossibleMoves.containsKey(Long.numberOfTrailingZeros(myPawn)))
                // объединяем атаку связывающей фигуры и возможности походить нашей
                allPossibleMoves.put(Long.numberOfTrailingZeros(myPawn),
                        getSidePawnMovesBitboard(chessBitboard, myPawn) & threatPieceBitboard & notMyPieces);
        }
        for (long myKnight : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getKnights())) {
            if (!allPossibleMoves.containsKey(Long.numberOfTrailingZeros(myKnight)))
                allPossibleMoves.put(Long.numberOfTrailingZeros(myKnight),
                        BitboardPatternsInitializer.knightMoveBitboards[Long.numberOfTrailingZeros(myKnight)]
                                & threatPieceBitboard & notMyPieces);
        }
        for (long myBishop : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getBishops())) {
            if (!allPossibleMoves.containsKey(Long.numberOfTrailingZeros(myBishop)))
                allPossibleMoves.put(Long.numberOfTrailingZeros(myBishop),
                        getBishopAllPossibleMovesBitboard(occupied,
                                new Coord(Long.numberOfTrailingZeros(myBishop))) & threatPieceBitboard & notMyPieces);
        }
        for (long myRook : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getRooks())) {
            if (!allPossibleMoves.containsKey(Long.numberOfTrailingZeros(myRook)))
                allPossibleMoves.put(Long.numberOfTrailingZeros(myRook),
                        getRookAllPossibleMovesBitboard(occupied,
                                new Coord(Long.numberOfTrailingZeros(myRook))) & threatPieceBitboard & notMyPieces);

        }
        for (long myQueen : BitUtils.segregatePositions(chessBitboard.getMyBitboards().getQueens())) {
            if (!allPossibleMoves.containsKey(Long.numberOfTrailingZeros(myQueen)))
                allPossibleMoves.put(Long.numberOfTrailingZeros(myQueen),
                        getQueenAllPossibleMovesBitboard(occupied,
                                new Coord(Long.numberOfTrailingZeros(myQueen))) & threatPieceBitboard & notMyPieces);
        }
        return allPossibleMoves;
    }
}


