package io.deeplay.api;

import io.deeplay.core.model.Side;
import io.deeplay.logic.BitUtils;
import io.deeplay.logic.ChessBoard;
import io.deeplay.model.ChessBitboard;
import io.deeplay.model.Coord;
import io.deeplay.model.SideBitboards;
import io.deeplay.parser.FENParser;
import org.junit.Test;

import java.util.Map;

import static io.deeplay.logic.BitUtils.containsSameBits;
import static org.junit.Assert.assertEquals;

public class TestAllOpponentsFiguresAttacks {

    // TODO: побольше тестов
    @Test
    public void testGetAllAttacks() {
        ChessBoard chessBoard = new ChessBoard("r4rk1/1pp1b1pp/p1n1b3/3n1pB1/2B5/1N3N2/PPP2PPP/R4RK1 b - - 7 19");
        Coord from = new Coord(BitUtils.BitIndex.G8_IDX.ordinal());
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(chessBoard.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        assertEquals(35, BitUtils.bitCount(BitboardHandler.getOpponentAttacksBitboardFromAllFigures(chessBitboard)));
    }

}
