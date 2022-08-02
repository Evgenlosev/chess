package io.deeplay.core.api;

public class TestKnightBitboardHandler {
    /*
    @Test
    public void getKnightMovesTest() {
        /*
         * Check possible G1 knight's moves in default position (F3, H3)
         *
        FENBoard FENBoard = new FENBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                new MoveInfo(new Coord(6, 0), new Coord(7, 2), MoveType.USUAL_MOVE, Figure.W_KNIGHT),
                new MoveInfo(new Coord(6, 0), new Coord(5, 2), MoveType.USUAL_MOVE, Figure.W_KNIGHT)
        ).collect(Collectors.toSet());
        assertEquals(expectedMoveInfoSet, BitboardHandler.getKnightMoves(FENBoard, new Coord(BitUtils.BitIndex.G1_IDX.ordinal())));

        /*
         * Check possible G1 knight's moves in default position (A3, C3)
         *
        expectedMoveInfoSet = Stream.of(
                new MoveInfo(new Coord(1, 0), new Coord(0, 2), MoveType.USUAL_MOVE, Figure.W_KNIGHT),
                new MoveInfo(new Coord(1, 0), new Coord(2, 2), MoveType.USUAL_MOVE, Figure.W_KNIGHT)
        ).collect(Collectors.toSet());
        assertEquals(expectedMoveInfoSet, BitboardHandler.getKnightMoves(FENBoard, new Coord(BitUtils.BitIndex.B1_IDX.ordinal())));

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
