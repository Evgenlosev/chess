package io.deeplay.core.api;

import io.deeplay.core.logic.BitboardPatternsInitializer;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.core.model.bitboard.CheckData;
import io.deeplay.core.model.bitboard.CheckType;
import io.deeplay.core.model.bitboard.ChessBitboard;
import io.deeplay.core.model.bitboard.SideBitboards;
import io.deeplay.core.parser.FENParser;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.deeplay.core.api.BitboardHandler.getAllPossibleMovesWrapped;

public class SimpleLogic implements SimpleLogicAppeal {

    @Override
    public boolean isMate(final String fenNotation) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboardsOld(fenNotation);
        Side mySide = FENParser.getTurnSide(fenNotation);
        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (mySide == Side.WHITE)
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (mySide == Side.BLACK)
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        Objects.requireNonNull(chessBitboard);

        CheckData mySideCheckData = BitboardHandler.getCheckData(chessBitboard);
        final ChessBitboard opponentChessBitboard =
                new ChessBitboard(chessBitboard.getOpponentSideBitboards(), chessBitboard.getProcessingSideBitboards()); // инвертируем стороны
        CheckData opponentSideCheckData = BitboardHandler.getCheckData(opponentChessBitboard);
        if (opponentSideCheckData.getCheckType().ordinal() > 0) {
            throw new IllegalArgumentException("Шах противнику, однако ход наш, такое не возможно");
        }
        final int kingIndex = Long.numberOfTrailingZeros(chessBitboard.getProcessingSideBitboards().getKing());
        long kingMoves = BitboardPatternsInitializer.kingMoveBitboards[kingIndex]
                & ~mySideCheckData.getAllAttacks() & ~chessBitboard.getProcessingSidePieces();
        // мат проверяем только для нашей стороны (т.к. отсекается случай когда противник под шахом, а ход наш)
        if (mySideCheckData.getCheckType() == CheckType.TWO && kingMoves == 0) {
            return true;
        }
        if (mySideCheckData.getCheckType() == CheckType.TWO && kingMoves != 0) {
            return true;
        }
        Map<Integer, Long> myAllPossibleMoves = BitboardHandler.getAllPossibleMoves(chessBitboard, mySideCheckData);
        final long allMoves = myAllPossibleMoves.values().stream().reduce((x, y) -> x | y).orElse(0L);
        return mySideCheckData.getCheckType() == CheckType.ONE && allMoves == 0L;
    }

    @Override
    public boolean isStalemate(final String fenNotation) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboardsOld(fenNotation);
        Side mySide = FENParser.getTurnSide(fenNotation);
        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (mySide == Side.WHITE)
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (mySide == Side.BLACK)
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        Objects.requireNonNull(chessBitboard);

        CheckData mySideCheckData = BitboardHandler.getCheckData(chessBitboard);
        final ChessBitboard opponentChessBitboard =
                new ChessBitboard(chessBitboard.getOpponentSideBitboards(), chessBitboard.getProcessingSideBitboards()); // инвертируем стороны
        CheckData opponentSideCheckData = BitboardHandler.getCheckData(opponentChessBitboard);
        if (opponentSideCheckData.getCheckType().ordinal() > 0) {
            throw new IllegalArgumentException("Шах противнику, однако ход наш, такое не возможно");
        }

        Map<Integer, Long> myAllPossibleMoves = BitboardHandler.getAllPossibleMoves(chessBitboard, mySideCheckData);
        return myAllPossibleMoves.entrySet().stream().noneMatch(x -> x.getValue() > 0L);
    }

    @Override
    public Set<MoveInfo> getMoves(final String fenNotation) {
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboardsOld(fenNotation);
        Side mySide = FENParser.getTurnSide(fenNotation);
        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (mySide == Side.WHITE)
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (mySide == Side.BLACK)
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        Objects.requireNonNull(chessBitboard);

        CheckData mySideCheckData = BitboardHandler.getCheckData(chessBitboard);

        Map<Integer, Long> myAllPossibleMoves = BitboardHandler.getAllPossibleMoves(chessBitboard, mySideCheckData);
        return getAllPossibleMovesWrapped(chessBitboard, myAllPossibleMoves);
    }

}
