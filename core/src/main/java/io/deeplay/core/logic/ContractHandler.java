package io.deeplay.core.logic;

import io.deeplay.core.api.BitboardHandler;
import io.deeplay.core.model.ChessBitboard;
import io.deeplay.core.model.CheckData;
import io.deeplay.core.model.CheckType;
import io.deeplay.core.parser.FENParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс, методы которого следят чтобы правила использования api не нарушались.
 * Кидает исключения в случае нарушения
 */
public class ContractHandler {
    // возвращает все возможные ходы для текущей стороны
    public static Map<Integer, Long> fenLogicVerification(final String fenNotation, final ChessBitboard chessBitboard) {
        Map<Integer, Long> myAllPossibleMoves = new HashMap<>();
        CheckData mySideCheckData = BitboardHandler.getCheckData(chessBitboard);
        final ChessBitboard opponentChessBitboard =
                new ChessBitboard(chessBitboard.getOpponentBitboards(), chessBitboard.getMyBitboards()); // инвертируем стороны
        CheckData opponentSideCheckData = BitboardHandler.getCheckData(opponentChessBitboard);

        // клетки под атакой & ходы короля
        if (opponentSideCheckData.getCheckType().ordinal() > 0
                && FENParser.getTurnSide(fenNotation) == chessBitboard.getMySide()) {
            throw new IllegalArgumentException("Шах противнику, однако ход наш, такое не возможно");
        }
        final int kingIndex = Long.numberOfTrailingZeros(chessBitboard.getMyBitboards().getKing());
        long kingMoves = BitboardPatternsInitializer.kingMoveBitboards[kingIndex]
                & ~mySideCheckData.getAllAttacks() & ~chessBitboard.getMyPieces();
        // мат проверяем только для нашей стороны (т.к. отсекается случай когда противник под шахом, а ход наш)
        if (mySideCheckData.getCheckType() == CheckType.TWO && kingMoves == 0) {
            throw new IllegalArgumentException
                    ("Стороне " + chessBitboard.getMySide() + " - мат, переданная строка содержит конечное состояние игры");
        }
        if (mySideCheckData.getCheckType() == CheckType.TWO && kingMoves != 0) {
            myAllPossibleMoves.put(kingIndex, kingMoves);
            return myAllPossibleMoves;
        }
        if (BitboardHandler.getAllPossibleMoves(opponentChessBitboard, opponentSideCheckData).entrySet()
                .stream().noneMatch(x -> x.getKey() > 0L)) {
            throw new IllegalArgumentException
                    ("Стороне " + opponentChessBitboard.getMySide() + " - пат, переданная строка содержит конечное состояние игры");
        }
        myAllPossibleMoves = BitboardHandler.getAllPossibleMoves(chessBitboard, mySideCheckData);
        if (myAllPossibleMoves.entrySet().stream().noneMatch(x -> x.getKey() > 0L)) {
            throw new IllegalArgumentException
                    ("Стороне " + chessBitboard.getMySide() + " - пат, переданная строка содержит конечное состояние игры");
        }
        final long allMoves = myAllPossibleMoves.values().stream().reduce((x, y) -> x | y).orElse(0L);
        if (mySideCheckData.getCheckType() == CheckType.ONE && allMoves == 0L) {
            throw new IllegalArgumentException
                    ("Стороне " + chessBitboard.getMySide() + " - мат, переданная строка содержит конечное состояние игры");
        }
        // TODO: для пешки надо будет использовать get, а так же считать ходы отдельно (т.к. в полученой из метода связывания мапе инфы о типе хода пешки не будет)
        return myAllPossibleMoves;
    }

}
