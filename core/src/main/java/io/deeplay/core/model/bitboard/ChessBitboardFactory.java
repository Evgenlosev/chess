package io.deeplay.core.model.bitboard;

import io.deeplay.core.logic.newlogic.SimpleBitboardHandler;
import io.deeplay.core.model.Side;

import java.util.Map;

public class ChessBitboardFactory {
    // TODO: if !list set.contains - список обязательных фигур для продолжения игр
    /*
    BitboardPatternsInitializer.whiteCells
    BitboardPatternsInitializer.blackCells
    Это где то отдельно считается
     * King versus king if (king & opponentKing == occupied)
     * King and bishop versus king - тут надо для обоих сторон, как и ниже
     * King and knight versus king
     * King and bishop versus king and bishop with the bishops on the same color. // Маска черных клеток, маска белых клеток
     * */
    // ChessBitboard изменяет состояние внутри, это можно исправить если все методы с ChessBitboard
    // перекрутить на SideBitboards, но это займет много времени
    public static ChessBitboard createChessBitboard(final Map<Side, SideBitboards> sideBitboards,
                                                    final Side currentTurnSide,
                                                    final long enPassant,
                                                    final String castlingRights) {
        SideBitboards currentTurnSideBitboards = sideBitboards.get(currentTurnSide);
        SideBitboards opponentSideBitboards = sideBitboards.get(Side.otherSide(currentTurnSide));

        ChessBitboard currentTurnChessBitboard = new ChessBitboard(currentTurnSideBitboards, opponentSideBitboards);
        currentTurnChessBitboard.setEnPassantFile(enPassant);
        currentTurnChessBitboard
                .setWhiteKingSideCastlingRight(castlingRights.contains("K"));
        currentTurnChessBitboard
                .setWhiteQueenSideCastlingRight(castlingRights.contains("Q"));
        currentTurnChessBitboard
                .setBlackKingSideCastlingRight(castlingRights.contains("k"));
        currentTurnChessBitboard
                .setBlackQueenSideCastlingRight(castlingRights.contains("q"));
        // Создаем фигуры и считаем возможные ходы
        currentTurnSideBitboards.initializePieceBitboards(currentTurnChessBitboard);
        opponentSideBitboards.initializePieceBitboards(currentTurnChessBitboard);
        // Собираем информацию о шахе, изменяет ChessBitboard
        currentTurnChessBitboard
                .setProcessingSideCheckData(SimpleBitboardHandler.getCheckData(currentTurnChessBitboard));

        // Добавляем информацию о возможных ходах внутри ChessBitboard, изменяет ChessBitboard
        SimpleBitboardHandler.countAllPossibleMoves(currentTurnChessBitboard);
        return currentTurnChessBitboard;
    }

}
