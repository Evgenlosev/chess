package io.deeplay.core.model.bitboard;

import io.deeplay.core.logic.newlogic.SimpleBitboardHandler;
import io.deeplay.core.model.Side;

import java.util.Map;

public class ChessBitboardFactory {
    // TODO: if !list set.contains - список обязательных фигур для продолжения игр
    /*
     * King versus king
     * King and bishop versus king
     * King and knight versus king
     * King and bishop versus king and bishop with the bishops on the same color.
     * */
    public static ChessBitboard createChessBitboard(final Map<Side, SideBitboards> sideBitboards,
                                                    final Side currentTurnSide,
                                                    final long enPassant) {
        SideBitboards currentTurnSideBitboards = sideBitboards.get(currentTurnSide);
        SideBitboards opponentSideBitboards = sideBitboards.get(Side.otherSide(currentTurnSide));

        ChessBitboard currentTurnChessBitboard = new ChessBitboard(currentTurnSideBitboards, opponentSideBitboards);
        currentTurnChessBitboard.setEnPassantFile(enPassant);
        // Создаем фигуры и считаем возможные ходы
        currentTurnSideBitboards.initializePieceBitboards(currentTurnChessBitboard);
        opponentSideBitboards.initializePieceBitboards(currentTurnChessBitboard);
        // Собираем информацию о шахе, для обоих сторон
        currentTurnChessBitboard.setProcessingSideCheckData(SimpleBitboardHandler.getCheckData(currentTurnChessBitboard));

        ChessBitboard opponentChessBitboard = new ChessBitboard(currentTurnSideBitboards, opponentSideBitboards);
        opponentChessBitboard.setProcessingSideCheckData(SimpleBitboardHandler
                .getCheckData(new ChessBitboard(opponentSideBitboards, currentTurnSideBitboards)));
        // TODO:
        // Собираем информацию о допустимых ходах(учитывая связанность, шах королю), для обоих сторон.
        // TODO: пат, шах противнику при нашем ходе

        return currentTurnChessBitboard;
    }

}
