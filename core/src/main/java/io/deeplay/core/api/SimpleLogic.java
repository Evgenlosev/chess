package io.deeplay.core.api;

import io.deeplay.core.logic.newlogic.SimpleBitboardHandler;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.bitboard.CheckType;
import io.deeplay.core.model.bitboard.ChessBitboard;
import io.deeplay.core.model.bitboard.SideBitboards;
import io.deeplay.core.parser.FENParser;

import java.util.Set;

import static io.deeplay.core.logic.newlogic.SimpleBitboardHandler.getCurrentProcessingSideAllMoves;


public class SimpleLogic implements SimpleLogicAppeal {

    @Override
    public boolean isMate(final String fenNotation) {
        ChessBitboard currentSideChessBitboard = FENParser.parseFENToBitboards(fenNotation);

        SideBitboards currentTurnSideBitboards = currentSideChessBitboard.getProcessingSideBitboards();
        SideBitboards opponentSideBitboards = currentSideChessBitboard.getOpponentSideBitboards();

        ChessBitboard opponentChessBitboard = new ChessBitboard(currentTurnSideBitboards, opponentSideBitboards);
        opponentChessBitboard.setProcessingSideCheckData(SimpleBitboardHandler
                .getCheckData(new ChessBitboard(opponentSideBitboards, currentTurnSideBitboards))); // invertedClone()?

        if (opponentChessBitboard.getProcessingSideCheckData().getCheckType().ordinal() > 0) {
            throw new IllegalArgumentException("Opponent is in check but it's our turn which is impossible");
        }
        // мат проверяем только для нашей стороны (т.к. отсекается случай когда противник под шахом, а ход наш)
        if (currentSideChessBitboard.getProcessingSideCheckData().getCheckType() == CheckType.TWO &&
                currentSideChessBitboard.getProcessingSideBitboards()
                        .getKingPieceBitboards().getMovesUnderRestrictions(currentSideChessBitboard) == 0) {
            return true;
        }

        return currentSideChessBitboard.getProcessingSideCheckData().getCheckType() == CheckType.ONE &&
                currentSideChessBitboard.getCountFiguresThatCanMove() == 0;
    }

    @Override
    public boolean isStalemate(final String fenNotation) {
        ChessBitboard currentSideChessBitboard = FENParser.parseFENToBitboards(fenNotation);

        SideBitboards currentTurnSideBitboards = currentSideChessBitboard.getProcessingSideBitboards();
        SideBitboards opponentSideBitboards = currentSideChessBitboard.getOpponentSideBitboards();

        ChessBitboard opponentChessBitboard = new ChessBitboard(currentTurnSideBitboards, opponentSideBitboards);
        opponentChessBitboard.setProcessingSideCheckData(SimpleBitboardHandler
                .getCheckData(new ChessBitboard(opponentSideBitboards, currentTurnSideBitboards)));
        SimpleBitboardHandler.countAllPossibleMoves(opponentChessBitboard);

        if (opponentChessBitboard.getProcessingSideCheckData().getCheckType().ordinal() > 0) {
            throw new IllegalArgumentException("Opponent is in check but it's our turn which is impossible");
        }
        return currentSideChessBitboard.getProcessingSideCheckData().getCheckType().ordinal() == 0
                && currentSideChessBitboard.getCountFiguresThatCanMove() == 0;
    }

    @Override
    public boolean isDrawByPieceShortage(final String fenNotation) {
        ChessBitboard currentSideChessBitboard = FENParser.parseFENToBitboards(fenNotation);

        SideBitboards currentTurnSideBitboards = currentSideChessBitboard.getProcessingSideBitboards();
        SideBitboards opponentSideBitboards = currentSideChessBitboard.getOpponentSideBitboards();

        ChessBitboard opponentChessBitboard = new ChessBitboard(currentTurnSideBitboards, opponentSideBitboards);
        opponentChessBitboard.setProcessingSideCheckData(SimpleBitboardHandler
                .getCheckData(new ChessBitboard(opponentSideBitboards, currentTurnSideBitboards)));
        SimpleBitboardHandler.countAllPossibleMoves(opponentChessBitboard);

        if (opponentChessBitboard.getProcessingSideCheckData().getCheckType().ordinal() > 0) {
            throw new IllegalArgumentException("Opponent is in check but it's our turn which is impossible");
        }
        final boolean isBishopsAndKingsLeft = (currentSideChessBitboard.getProcessingSideBitboards().getKing() |
                currentSideChessBitboard.getProcessingSideBitboards().getBishops() |
                currentSideChessBitboard.getOpponentSideBitboards().getKing() |
                currentSideChessBitboard.getOpponentSideBitboards().getBishops()) ==
                currentSideChessBitboard.getOccupied();

        final boolean isKnightsAndKingsLeft = (currentSideChessBitboard.getProcessingSideBitboards().getKing() |
                currentSideChessBitboard.getProcessingSideBitboards().getKnights() |
                currentSideChessBitboard.getOpponentSideBitboards().getKing() |
                currentSideChessBitboard.getOpponentSideBitboards().getKnights()) ==
                currentSideChessBitboard.getOccupied();

        if ((currentSideChessBitboard.getProcessingSideBitboards().getKing() |
                currentSideChessBitboard.getOpponentSideBitboards().getKing()) ==
                currentSideChessBitboard.getOccupied())
            return true;

        if (currentSideChessBitboard.isOneBishop() && isBishopsAndKingsLeft)
            return true;

        if (currentSideChessBitboard.isOneKnight() && isKnightsAndKingsLeft)
            return true;
        return currentSideChessBitboard.isLeftBishopsOnAlikeCellColors() && isBishopsAndKingsLeft;
    }

    @Override
    public Set<MoveInfo> getMoves(final String fenNotation) {
        // TODO: проверку на isDrawByPieceShortage перед ходом, чтобы в случае конца игры вернуть 0 ходов, вынести метод с параметром ChessBitboard
        return getCurrentProcessingSideAllMoves(FENParser.parseFENToBitboards(fenNotation));
    }

}
