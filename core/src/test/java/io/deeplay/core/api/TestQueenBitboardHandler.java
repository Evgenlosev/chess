package io.deeplay.core.api;

import io.deeplay.core.logic.BitUtils;
import io.deeplay.core.model.Coord;
import io.deeplay.core.model.Figure;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.MoveType;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.deeplay.core.logic.BitUtils.BitIndex.D4_IDX;
import static io.deeplay.core.logic.BitUtils.BitIndex.D5_IDX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestQueenBitboardHandler {

    private final static SimpleLogicAppeal simpleLogicAppeal = new SimpleLogic();

    @Test
    public void testGetQueenMovesAtStartingPosition() {
        String fenNotation = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        assertTrue(simpleLogicAppeal.getMoves(fenNotation)
                .stream().filter(pieceMoves -> pieceMoves.getFigure() == Figure.W_QUEEN
                ).collect(Collectors.toSet()).isEmpty());
    }

    @Test
    public void testGetQueenMoves() {
        String fenNotation = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        BitUtils.BitIndex startingPosition = D4_IDX;

        Set<MoveInfo> pawnMoves = Stream.of(
                new MoveInfo(new Coord(startingPosition.ordinal()), new Coord(D5_IDX.ordinal()), MoveType.USUAL_MOVE, Figure.W_PAWN)
        ).collect(Collectors.toSet());

        assertEquals(pawnMoves, simpleLogicAppeal.getMoves(fenNotation)
                .stream().filter(pieceMoves -> pieceMoves.getFigure() == Figure.W_PAWN
                        && pieceMoves.getCellFrom().getIndexAsOneDimension() == startingPosition.ordinal()
                ).collect(Collectors.toSet()));
    }

/*
    @Test
    public void testQueenAtStartPositionClassic() {
        FENBoard FENBoard = new FENBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"); // D1 белые
        assertEquals(0, BitboardHandler.getQueenMoves(FENBoard, new Coord(BitUtils.BitIndex.D1_IDX.ordinal())).size());
    }

    @Test
    public void testQueenInMiddlePosition() {
        FENBoard FENBoard = new FENBoard("8/N1k1r1R1/P1P1b2P/1P2Q1b1/4P3/1nN3K1/8/3r1r2 b - - 0 1"); // черные e5 7
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(36), new Coord(50), MoveType.USUAL_ATTACK, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(43), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(44), MoveType.USUAL_ATTACK, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(45), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(34), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(35), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(37), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(38), MoveType.USUAL_ATTACK, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(27), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(29), MoveType.USUAL_MOVE, Figure.W_QUEEN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getQueenMoves(FENBoard, new Coord(BitUtils.BitIndex.E5_IDX.ordinal())));

    }

    @Test
    public void testQueenInMiddlePositionClamped() { // королева зажата
        FENBoard FENBoard = new FENBoard("8/8/3pnB2/R3QK2/4pr2/2k5/8/8 w - - 0 1"); // белые
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(36), new Coord(43), MoveType.USUAL_ATTACK, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(44), MoveType.USUAL_ATTACK, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(33), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(34), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(27), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(28), MoveType.USUAL_ATTACK, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(29), MoveType.USUAL_ATTACK, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(35), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(36), new Coord(18), MoveType.USUAL_ATTACK, Figure.W_QUEEN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getQueenMoves(FENBoard, new Coord(BitUtils.BitIndex.E5_IDX.ordinal())));
    }

    @Test
    public void testQueensInCornersPosition() {
        // 31 - белый (у одного края), 0 - черный (в углу)
        FENBoard FENBoard = new FENBoard("rnb1kbnr/1p2p1pp/3p4/p1p5/5p1Q/4P3/2PP1PPP/qNB1KBNR w Kkq a6 0 1");
        Set<MoveInfo> expectedWhiteMoveInfoSet = Stream.of( // белая королева A1 - 0 клетка
                        new MoveInfo(new Coord(31), new Coord(22), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(31), new Coord(30), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(31), new Coord(39), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(31), new Coord(23), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(31), new Coord(29), MoveType.USUAL_ATTACK, Figure.W_QUEEN),
                        new MoveInfo(new Coord(31), new Coord(52), MoveType.USUAL_ATTACK, Figure.W_QUEEN),
                        new MoveInfo(new Coord(31), new Coord(55), MoveType.USUAL_ATTACK, Figure.W_QUEEN),
                        new MoveInfo(new Coord(31), new Coord(47), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(31), new Coord(38), MoveType.USUAL_MOVE, Figure.W_QUEEN),
                        new MoveInfo(new Coord(31), new Coord(45), MoveType.USUAL_MOVE, Figure.W_QUEEN))
                .collect(Collectors.toSet());
        assertEquals(expectedWhiteMoveInfoSet, BitboardHandler.getQueenMoves(FENBoard, new Coord(BitUtils.BitIndex.H4_IDX.ordinal())));

        Set<MoveInfo> expectedBlackMoveInfoSet = Stream.of( // черная королева H2 - 15 клетка
                        new MoveInfo(new Coord(0), new Coord(1), MoveType.USUAL_ATTACK, Figure.B_QUEEN),
                        new MoveInfo(new Coord(0), new Coord(24), MoveType.USUAL_MOVE, Figure.B_QUEEN),
                        new MoveInfo(new Coord(0), new Coord(45), MoveType.USUAL_MOVE, Figure.B_QUEEN),

                        new MoveInfo(new Coord(0), new Coord(27), MoveType.USUAL_MOVE, Figure.B_QUEEN),
                        new MoveInfo(new Coord(0), new Coord(36), MoveType.USUAL_MOVE, Figure.B_QUEEN),
                        new MoveInfo(new Coord(0), new Coord(9), MoveType.USUAL_MOVE, Figure.B_QUEEN),

                        new MoveInfo(new Coord(0), new Coord(18), MoveType.USUAL_MOVE, Figure.B_QUEEN),
                        new MoveInfo(new Coord(0), new Coord(8), MoveType.USUAL_MOVE, Figure.B_QUEEN),
                        new MoveInfo(new Coord(0), new Coord(16), MoveType.USUAL_MOVE, Figure.B_QUEEN))
                .collect(Collectors.toSet());

        assertEquals(expectedBlackMoveInfoSet, BitboardHandler.getQueenMoves(FENBoard, new Coord(BitUtils.BitIndex.A1_IDX.ordinal())));
    }
 */
    
}
