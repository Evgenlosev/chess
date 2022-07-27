package io.deeplay.logic.api;

import io.deeplay.core.model.Coord;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.logic.logic.BitUtils;
import io.deeplay.logic.logic.FENBoard;
import io.deeplay.logic.model.ChessBitboard;
import io.deeplay.logic.model.SideBitboards;
import io.deeplay.logic.parser.FENParser;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.deeplay.logic.logic.BitUtils.containsSameBits;
import static org.junit.Assert.assertEquals;

public class TestAllOpponentsFigures {
    @Test
    public void testCompareAttacksAndPossibilitiesWhite() {
        FENBoard FENBoard =
                new FENBoard("r4rk1/1pp1b1pp/p1n1b3/3n1pB1/2B5/1N3N2/PPP2PPP/R4RK1 b - - 7 19");
        Coord from = new Coord(BitUtils.BitIndex.G8_IDX.ordinal());
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(FENBoard.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        long allAttacks = BitboardHandler.getOpponentAttacksBitboardFromAllFigures(chessBitboard); // оппонент - былые
        long allExpectedAttacks = 0b0000000000010000101000010101111110101000111111111111100111111111L;
        // Клетки под УГРОЗОЙ атаки (но не факт, что возможно походить)
        assertEquals(35, BitUtils.bitCount(allAttacks));
        assertEquals(allExpectedAttacks, allAttacks);

        // Клетки на которые есть возможность походить
        // (для пешек, например, нет возможности атаковать клетку без фигуры противника на ней)
        List<Coord> allCoords = BitboardHandler.getAllPossibleMoves(FENBoard, Side.WHITE)
                .values().stream().map(MoveInfo::getCellTo).collect(Collectors.toList());
        Set<Coord> uniqueCoords = new HashSet<>(allCoords);

        /*
        // Рекомендую для понимая различия выводить биты по из-за которых есть отличие в количестве угроз(атак)
        // и возможностях походить

        long allMoves = 0L;
        for(Coord coord : uniqueCoords){
            allMoves |= 1L << coord.getIndexAsOneDimension();
        }
        BitUtils.printBitboard(allMoves ^ allAttacks);
        */

        assertEquals(39, allCoords.size());
        assertEquals(27, uniqueCoords.size());

        FENBoard = new FENBoard("4r1k1/1pp4p/p7/B5p1/1PQ5/5P1b/P5PP/3K1R2 b - - 0 1");
        // TODO: с таким же FEN тест на то что у короля 3 хода + 1 ладьи + 1 слона = 5 возможных ходов
        from = new Coord(BitUtils.BitIndex.G8_IDX.ordinal());
        sideBitboards = FENParser.parseFENToBitboards(FENBoard.getFenNotation());

        chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        // Клетки под атакой
        allAttacks = BitboardHandler.getOpponentAttacksBitboardFromAllFigures(chessBitboard); // оппонент - былые
        allExpectedAttacks = 0b0100000000100100000101110000111111111010111011100011110111111100L;
        assertEquals(34, BitUtils.bitCount(allAttacks));
        assertEquals(allExpectedAttacks, allAttacks);

        // Возможные ходы
        allCoords = BitboardHandler.getAllPossibleMoves(FENBoard, Side.WHITE)
                .values().stream().map(MoveInfo::getCellTo).collect(Collectors.toList());
        uniqueCoords = new HashSet<>(allCoords);

        assertEquals(38, allCoords.size());
        assertEquals(30, uniqueCoords.size());
    }

    @Test
    public void testCompareAttacksAndPossibilitiesBlack() {
        FENBoard FENBoard =
                new FENBoard("r4rk1/1pp1b1pp/p1n1b3/3n1pB1/2B5/1N3N2/PPP2PPP/R4RK1 b - - 7 19");
        Coord from = new Coord(BitUtils.BitIndex.G1_IDX.ordinal());
        Map<Side, SideBitboards> sideBitboards = FENParser.parseFENToBitboards(FENBoard.getFenNotation());

        ChessBitboard chessBitboard = null;
        // Определяем стороны
        if (containsSameBits(sideBitboards.get(Side.WHITE).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.WHITE), sideBitboards.get(Side.BLACK));
        if (containsSameBits(sideBitboards.get(Side.BLACK).getKing(), 1L << from.getIndexAsOneDimension()))
            chessBitboard = new ChessBitboard(sideBitboards.get(Side.BLACK), sideBitboards.get(Side.WHITE));
        if (chessBitboard == null)
            throw new IllegalArgumentException("Координата не соответствует фигуре на доске");

        // Клетки под атакой
        long allAttacks = BitboardHandler.getOpponentAttacksBitboardFromAllFigures(chessBitboard); // оппонент - чёрные
        long allExpectedAttacks = 0b1111111111111101111011110111111101111010000101010000000000000000L;
        assertEquals(37, BitUtils.bitCount(allAttacks));
        assertEquals(allExpectedAttacks, allAttacks);

        // Возможные ходы
        List<Coord> allCoords = BitboardHandler.getAllPossibleMoves(FENBoard, Side.BLACK)
                .values().stream().map(MoveInfo::getCellTo).collect(Collectors.toList());
        Set<Coord> uniqueCoords = new HashSet<>(allCoords);

        assertEquals(43, allCoords.size());
        assertEquals(25, uniqueCoords.size());
    }

}
