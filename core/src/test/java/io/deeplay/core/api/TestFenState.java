package io.deeplay.core.api;

import io.deeplay.core.logic.BitUtils;
import io.deeplay.core.logic.ContractHandler;
import io.deeplay.core.logic.FENBoard;
import io.deeplay.core.logic.newlogic.SimpleBitboardHandler;
import io.deeplay.core.model.Coord;
import io.deeplay.core.model.Side;
import io.deeplay.core.model.bitboard.ChessBitboard;
import io.deeplay.core.model.bitboard.SideBitboards;
import io.deeplay.core.parser.FENParser;
import org.junit.Test;

import java.util.Map;

import static io.deeplay.core.logic.BitUtils.containsSameBits;
import static io.deeplay.core.logic.BitUtils.printBitboard;

public class TestFenState {

    // TODO: большеееее тестов
    @Test
    public void testStalemate() {
//        String fenNotation = "8/2k5/5r2/4p3/4P3/4K3/3r4/8 w - - 0 1";
        String fenNotation = "8/2k5/5r2/4p3/4P3/4K3/3r4/1n6 w - - 0 1";
        FENBoard fenBoard = new FENBoard(fenNotation);
        Coord from = new Coord(BitUtils.BitIndex.E3_IDX.ordinal());
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboardsOld(fenBoard.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        Map<Integer, Long> allPossibleMoves = ContractHandler.fenLogicVerification(fenNotation, chessBitboard);

        long allMoves = 0L;
        for (long moves : allPossibleMoves.values()) {
            allMoves |= moves;
        }
        printBitboard(allMoves);
        System.out.println(allPossibleMoves);
    }

    @Test
    public void testStalemateNew() {
//        String fenNotation = "7q/1k6/8/5N2/4K2r/2n5/8/2b5 w - - 0 1"; // королю двойной шах, 2 возможных хода
//        String fenNotation = "7q/1k6/8/5N2/4K2r/8/8/2b5 w - - 0 1"; // королю один шах, 4 возможных хода (конь может срубить угрозу)
//        String fenNotation = "8/8/8/8/2k5/8/3K4/8 b - - 0 1"; // дуэль королей -2 хода у чёрного короля
//        String fenNotation = "8/8/8/8/3KRq1k/8/8/8 w - - 0 1";
        String fenNotation = "5k2/6p1/8/6K1/8/8/8/8 w - - 0 1";
        ChessBitboard chessBitboard = FENParser.parseFENToBitboards(fenNotation);

        long allMoves = 0L;
        for (long moves : SimpleBitboardHandler.getAllPossibleMoves(chessBitboard).values()) {
            allMoves |= moves;
        }
        printBitboard(allMoves);

    }
}
