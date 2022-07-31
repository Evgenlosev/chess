package io.deeplay.core.logic.newlogic;

import io.deeplay.core.logic.BitboardDynamicPatterns;
import io.deeplay.core.logic.BitboardPatternsInitializer;
import io.deeplay.core.model.Figure;
import io.deeplay.core.model.Side;
import io.deeplay.core.model.bitboard.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.deeplay.core.logic.BitUtils.containsSameBits;
import static io.deeplay.core.logic.BitUtils.inBetween;
import static java.util.Map.entry;

// TODO: невозможно чтобы шах был обоим королям одновременно

/**
 * Класс который должен заменить BitboardHandler, в силу читаемости
 */
public class SimpleBitboardHandler {
    /**
     * @param chessBitboard содержит всю информацию о доске которая может понадобиться для вычисления ходов.
     * @param ignoredPiece снятие фигур указанных(в маске битборда) позиций на занятых(occupied) клетках,
     * нужно линейно-ходящим фигурам.
     * @param restriction ограничения на возможные ходы, нужно для получения множества допустимых ходов при связности.
     * @param from на какой позиции находится фигура ходы которой мы считаем.
     * @return битборд допустимых ходов.
     */
    public static final QuadFunction<ChessBitboard, Long, Long, Integer, Long> getRookMovesBitboard =
            (chessBitboard, ignoredPiece, restriction, from) -> {
                final MagicBoard magic = BitboardPatternsInitializer.rookMagicBoards[from];
                return magic.moveBoards[(int) (((chessBitboard.getOccupied() ^ ignoredPiece) & magic.blockerMask) *
                        BitboardPatternsInitializer.ROOK_MAGIC_NUMBERS[from] >>> magic.shift)] & restriction;
            };
    public static final QuadFunction<ChessBitboard, Long, Long, Integer, Long> getBishopMovesBitboard =
            (chessBitboard, ignoredPiece, restriction, from) -> {
                final MagicBoard magic = BitboardPatternsInitializer.bishopMagicBoards[from];
                return magic.moveBoards[(int) (((chessBitboard.getOccupied() ^ ignoredPiece) & magic.blockerMask) *
                        BitboardPatternsInitializer.BISHOP_MAGIC_NUMBERS[from] >>> magic.shift)] & restriction;
            };
    public static final QuadFunction<ChessBitboard, Long, Long, Integer, Long> getQueenMovesBitboard =
            (chessBitboard, ignoredPiece, restriction, from) ->
                    getBishopMovesBitboard.apply(chessBitboard, ignoredPiece, restriction, from)
                            | getRookMovesBitboard.apply(chessBitboard, ignoredPiece, restriction, from);
    public static final QuadFunction<ChessBitboard, Long, Long, Integer, Long> getKnightMovesBitboard =
            (chessBitboard, ignoredPiece, restriction, from) ->
                    BitboardPatternsInitializer.knightMoveBitboards[from] & restriction;
    public static final QuadFunction<ChessBitboard, Long, Long, Integer, Long> getKingMovesBitboard =
            (chessBitboard, ignoredPiece, restriction, from) ->
                    BitboardPatternsInitializer.kingMoveBitboards[from] & restriction;
    public static final QuadFunction<ChessBitboard, Long, Long, Integer, Long> getWhitePawnMovesBitboard =
            (chessBitboard, ignoredPiece, restriction, from) ->
                    BitboardDynamicPatterns.possibleWhitePawnMovesBitboard(chessBitboard, from) & restriction;
    public static final QuadFunction<ChessBitboard, Long, Long, Integer, Long> getBlackPawnMovesBitboard =
            (chessBitboard, ignoredPiece, restriction, from) ->
                    BitboardDynamicPatterns.possibleBlackPawnMovesBitboard(chessBitboard, from) & restriction;
    /**
     * Пешки двигаются по-разному, в зависимости от стороны, поэтому нужно учитывать какая сторона будет двигать пешку.
     * В мапе хранятся только атаки пешек, остальные виды передвижений пешек, считаются отдельно,
     * в классе {@link io.deeplay.core.logic.BitboardDynamicPatterns}.
     */
    private static final Map<Side, List<Long>> sidePawnAttacks = new EnumMap<>(
            Map.ofEntries(
                    entry(Side.WHITE, BitboardPatternsInitializer.whitePawnMoveBitboards),
                    entry(Side.BLACK, BitboardPatternsInitializer.blackPawnMoveBitboards)
            ));

    public static QuadFunction<ChessBitboard, Long, Long, Integer, Long> getPawnFunction(final Side side) {
        return side == Side.WHITE ? getWhitePawnMovesBitboard : getBlackPawnMovesBitboard;
    }

    public static CheckData getCheckData(final ChessBitboard chessBitboard) {
        final long myKingBitboard = chessBitboard.getProcessingSideBitboards().getKing();
        final int kingIndex = chessBitboard.getProcessingSideBitboards().getKingPieceBitboards().getPositionIndex();
        int countChecks = 0;
        long pieceMoves;
        long allAttacks = 0L;
        long threateningPiecePositionBitboard = 0L;
        // тут учитывается, позиция короля под атакой (главное не срубить короля случайно)
        long threatPieceBitboard = 0L;
        for (PieceBitboard pieceBitboard : chessBitboard.getOpponentSideBitboards().getPieceBitboards()) {
            pieceMoves = pieceBitboard.getMovesWithIgnoredPiece(chessBitboard, myKingBitboard);
            // В pieceMoves могли попасть ходы пешек, а должны только атаки
            if (pieceBitboard.getFigure() == Figure.W_PAWN || pieceBitboard.getFigure() == Figure.B_PAWN)
                pieceMoves = sidePawnAttacks.get(pieceBitboard.getSide()).get(pieceBitboard.getPositionIndex());
            if (containsSameBits(pieceMoves, myKingBitboard)) {
                countChecks++;
                threatPieceBitboard |= inBetween(kingIndex, pieceBitboard.getPositionIndex());
                threateningPiecePositionBitboard |= pieceBitboard.getPositionBitboard();
            }
            allAttacks |= pieceMoves;
        }
        allAttacks |= chessBitboard.getOpponentSideBitboards().getKingPieceBitboards().getAllMovesBitboard();
        if (countChecks > 1)
            return new CheckData(CheckType.TWO, 0L, 0L, allAttacks);
        if (countChecks == 1)
            return new CheckData(CheckType.ONE,
                    threatPieceBitboard ^ myKingBitboard,
                    threateningPiecePositionBitboard,
                    allAttacks);
        // ~0L, т.к. нету угрожающей фигуры, поэтому фигуры могут ходить ПО ВСЕМ клеткам
        return new CheckData(CheckType.NONE, ~0L, 0L, allAttacks);
    }

    // Нужно получать все ходы обеим сторонам, чтобы проверить, что fen не в пате.
    // После этого метода во всех PieceBitboard появляется информация о не доступных для хода клетках.
    public static Map<Integer, Long> getAllPossibleMoves(final ChessBitboard chessBitboard) {
        Objects.requireNonNull(chessBitboard.getProcessingSideCheckData());
        Map<Integer, Long> allPossibleMoves = new HashMap<>();
        final long processingSideKing = chessBitboard.getProcessingSideBitboards().getKing();
        final int kingIndex = chessBitboard.getProcessingSideBitboards().getKingPieceBitboards().getPositionIndex();
        final long notMyPieces = ~chessBitboard.getProcessingSidePieces();
        long opponentPieceAttack;
        final long threatPieceBitboard = chessBitboard.getProcessingSideCheckData().getThreatPieceBitboard();
        final long threateningPiecePositionBitboard =
                chessBitboard.getProcessingSideCheckData().getThreateningPiecePositionBitboard();
        final Set<Figure> slidingPiecesFigures = Stream.of(
                Figure.W_BISHOP, Figure.W_ROOK, Figure.W_QUEEN,
                Figure.B_BISHOP, Figure.B_ROOK, Figure.B_QUEEN).collect(Collectors.toSet()); // все линейно-ходящие фигуры
        final List<PieceBitboard> opponentSlidingPieces = chessBitboard.getOpponentSideBitboards().getPieceBitboards()
                .stream().filter(piece -> slidingPiecesFigures.contains(piece.getFigure())).collect(Collectors.toList());
        // Считаем связанность.
        // Можно воспринимать каждую фигуру как стенку, каждую из которых мы по очереди снимаем.
        for (PieceBitboard opponentPieceBitboard : opponentSlidingPieces) {
            if (opponentPieceBitboard.getPositionBitboard() == threateningPiecePositionBitboard)
                continue; // Для фигуры которая поставила шах связанность считать не надо.
            for (PieceBitboard processingSidePieceBitboard :
                    chessBitboard.getProcessingSideBitboards().getPieceBitboards()) {

                opponentPieceAttack = opponentPieceBitboard // xray-атака (атака без учета текущей фигуры)
                        .getMovesWithIgnoredPiece(chessBitboard, processingSidePieceBitboard.getPositionBitboard());
                if (containsSameBits(opponentPieceAttack, processingSideKing)) { // Связанная фигура.
                    // Объединяем возможные ходы фигуры, xray-атаку связывающей фигуры и атаки фигуры которая поставила шах
                    processingSidePieceBitboard.addRestriction(
                            (inBetween(kingIndex, opponentPieceBitboard.getPositionIndex()) ^ processingSideKing)
                                    & threatPieceBitboard); // Если шах, то не должны учитываться другие способы ходить
                    allPossibleMoves.put(processingSidePieceBitboard.getPositionIndex(),
                            processingSidePieceBitboard.getMovesUnderRestrictions(chessBitboard));
                }
            }
        }
        // Здесь смотрим фигуры которые не связаны.
        // Объединяем позицию и атаку фигуры поставившей шах с возможностью походить нашей.
        for (PieceBitboard processingSidePieceBitboard :
                chessBitboard.getProcessingSideBitboards().getPieceBitboards()) {

            if (!allPossibleMoves.containsKey(processingSidePieceBitboard.getPositionIndex())) {
                processingSidePieceBitboard.addRestriction(threatPieceBitboard & notMyPieces);
                allPossibleMoves.put(processingSidePieceBitboard.getPositionIndex(),
                        processingSidePieceBitboard.getMovesUnderRestrictions(chessBitboard));
            }
        }
        // считаем допустимые ходы для короля
        chessBitboard.getProcessingSideBitboards().getKingPieceBitboards().addRestriction(
                ~chessBitboard.getProcessingSideCheckData().getAllAttacks() // Клетки НЕ под атакой.
                        & ~chessBitboard.getProcessingSidePieces()); // Нельзя нападать на своих.
        allPossibleMoves.put(kingIndex, chessBitboard
                .getProcessingSideBitboards().getKingPieceBitboards().getMovesUnderRestrictions(chessBitboard));
        return allPossibleMoves;
    }

    /*
    /**
     * Метод оборачивает все битборды в класс MoveInfo
     *
     * @param chessBitboard    текущее состояние доски
     * @param from             откуда ходим
     * @param allPossibleMoves маска всех возможных ходов фигуры
     * @param figure           сама фигура
     * @return множество всех возможных ходов

    private static Set<MoveInfo> wrapUpMoves(final ChessBitboard chessBitboard,
                                             final Coord from,
                                             final long allPossibleMoves,
                                             final Figure figure) {
        Set<MoveInfo> movesInfo = new HashSet<>();
        final long notMyPieces = ~chessBitboard.getProcessingSidePieces();
        final long notOpponentPieces = ~chessBitboard.getOpponentPieces();
        for (long possibleMove : BitUtils.segregatePositions(allPossibleMoves)) {
            if (containsSameBits(possibleMove, chessBitboard.getOpponentPieces()))
                movesInfo.add(new MoveInfo(from, new Coord(Long.numberOfTrailingZeros(possibleMove)),
                        MoveType.USUAL_ATTACK, figure));
            if (containsSameBits(possibleMove, notMyPieces & notOpponentPieces))
                movesInfo.add(new MoveInfo(from, new Coord(Long.numberOfTrailingZeros(possibleMove)),
                        MoveType.USUAL_MOVE, figure));
        }
        return movesInfo;
    }
     */
}
