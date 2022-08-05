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

import static io.deeplay.core.logic.BitUtils.BitIndex.*;
import static org.junit.Assert.assertEquals;

public class TestKnightBitboardHandler {

    private final static SimpleLogicAppeal simpleLogicAppeal = new SimpleLogic();

    @Test
    public void testGetWhiteKnightMovesAtStartingPosition() {
        String fenNotation = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        BitUtils.BitIndex startingPosition = B1_IDX;

        Set<MoveInfo> knightMoves = Stream.of(
                new MoveInfo(new Coord(startingPosition.ordinal()), new Coord(A3_IDX.ordinal()), MoveType.USUAL_MOVE, Figure.W_KNIGHT),
                new MoveInfo(new Coord(startingPosition.ordinal()), new Coord(C3_IDX.ordinal()), MoveType.USUAL_MOVE, Figure.W_KNIGHT)
        ).collect(Collectors.toSet());

        assertEquals(knightMoves, simpleLogicAppeal.getMoves(fenNotation)
                .stream().filter(pieceMoves -> pieceMoves.getFigure() == Figure.W_KNIGHT
                        && pieceMoves.getCellFrom().getIndexAsOneDimension() == startingPosition.ordinal()
                ).collect(Collectors.toSet()));
    }

    @Test
    public void testGetBlackKnightMovesAtStartingPosition() {
        String fenNotation = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1";

        BitUtils.BitIndex startingPosition = G8_IDX;

        Set<MoveInfo> knightMoves = Stream.of(
                new MoveInfo(new Coord(startingPosition.ordinal()), new Coord(F6_IDX.ordinal()), MoveType.USUAL_MOVE, Figure.B_KNIGHT),
                new MoveInfo(new Coord(startingPosition.ordinal()), new Coord(H6_IDX.ordinal()), MoveType.USUAL_MOVE, Figure.B_KNIGHT)
        ).collect(Collectors.toSet());

        assertEquals(knightMoves, simpleLogicAppeal.getMoves(fenNotation)
                .stream().filter(pieceMoves -> pieceMoves.getFigure() == Figure.B_KNIGHT
                        && pieceMoves.getCellFrom().getIndexAsOneDimension() == startingPosition.ordinal()
                ).collect(Collectors.toSet()));
    }

    /*
    @Test
    public void getKnightMovesTest() {
        /*
         * Check possible F7 knight's moves after
         * 1.e4e5 2.Nf3Nc6 3.Bc4Nf6 4.Ng5h6 5.Nxf7Nxe4
         * Expected:D8 D6 E5 G5 H6 H8
         *
        FENBoard = new FENBoard("r1bqkb1r/pppp1Np1/2n4p/4p3/2B1n3/8/PPPP1PPP/RNBQK2R w KQkq - 0 1");
        expectedMoveInfoSet = Stream.of(
                new MoveInfo(new Coord(5, 6), new Coord(7, 7), MoveType.USUAL_ATTACK, Figure.W_KNIGHT),
                new MoveInfo(new Coord(5, 6), new Coord(7, 5), MoveType.USUAL_ATTACK, Figure.W_KNIGHT),
                new MoveInfo(new Coord(5, 6), new Coord(6, 4), MoveType.USUAL_MOVE, Figure.W_KNIGHT),
                new MoveInfo(new Coord(5, 6), new Coord(4, 4), MoveType.USUAL_ATTACK, Figure.W_KNIGHT),
                new MoveInfo(new Coord(5, 6), new Coord(3, 5), MoveType.USUAL_MOVE, Figure.W_KNIGHT),
                new MoveInfo(new Coord(5, 6), new Coord(3, 7), MoveType.USUAL_ATTACK, Figure.W_KNIGHT)
        ).collect(Collectors.toSet());
        assertEquals(expectedMoveInfoSet, BitboardHandler.getKnightMoves(FENBoard, new Coord(BitUtils.BitIndex.F7_IDX.ordinal())));

        /*
         * Check possible C6 knight's moves after
         * 1.e4e5 2.Nf3Nc6 3.Bc4Nf6 4.Ng5h6 5.Nxf7Nxe4 6.Nxd8
         * Expected:B8 D8 E7 D4 B4 A5
         *
        FENBoard = new FENBoard("r1bNkb1r/pppp2p1/2n4p/4p3/2B1n3/8/PPPP1PPP/RNBQK2R b KQkq - 0 1");
        expectedMoveInfoSet = Stream.of(
                new MoveInfo(new Coord(2, 5), new Coord(3, 7), MoveType.USUAL_ATTACK, Figure.B_KNIGHT),
                new MoveInfo(new Coord(2, 5), new Coord(4, 6), MoveType.USUAL_MOVE, Figure.B_KNIGHT),
                new MoveInfo(new Coord(2, 5), new Coord(3, 3), MoveType.USUAL_MOVE, Figure.B_KNIGHT),
                new MoveInfo(new Coord(2, 5), new Coord(1, 3), MoveType.USUAL_MOVE, Figure.B_KNIGHT),
                new MoveInfo(new Coord(2, 5), new Coord(0, 4), MoveType.USUAL_MOVE, Figure.B_KNIGHT),
                new MoveInfo(new Coord(2, 5), new Coord(1, 7), MoveType.USUAL_MOVE, Figure.B_KNIGHT)
        ).collect(Collectors.toSet());
        assertEquals(expectedMoveInfoSet, BitboardHandler.getKnightMoves(FENBoard, new Coord(BitUtils.BitIndex.C6_IDX.ordinal())));
    }
     */
}
