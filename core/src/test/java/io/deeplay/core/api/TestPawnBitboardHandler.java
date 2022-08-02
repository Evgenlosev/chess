package io.deeplay.core.api;

public class TestPawnBitboardHandler {
/*
    @Test
    public void testPawnAtStartPositionClassic() {
        FENBoard FENBoard = new FENBoard("rnbqkbnr/ppp1p1pp/8/3p1p2/4P3/1P6/P1PP1PPP/RNBQKBNR b KQkq - 0 1");
        Set<MoveInfo> expectedWhiteMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(11), new Coord(19), MoveType.USUAL_MOVE, Figure.W_PAWN),
                        new MoveInfo(new Coord(11), new Coord(27), MoveType.PAWN_LONG_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedWhiteMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.D2_IDX.ordinal())));

        Set<MoveInfo> expectedBlackMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(37), new Coord(28), MoveType.PAWN_ATTACK, Figure.B_PAWN),
                        new MoveInfo(new Coord(37), new Coord(29), MoveType.USUAL_MOVE, Figure.B_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedBlackMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.F5_IDX.ordinal())));

        expectedBlackMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(55), new Coord(47), MoveType.USUAL_MOVE, Figure.B_PAWN),
                        new MoveInfo(new Coord(55), new Coord(39), MoveType.PAWN_LONG_MOVE, Figure.B_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedBlackMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.H7_IDX.ordinal())));

        expectedWhiteMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(17), new Coord(25), MoveType.USUAL_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedWhiteMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.B3_IDX.ordinal())));

        FENBoard = new FENBoard("rnbqkbnr/ppp1pppp/1P6/8/8/3p4/P1PPPPPP/RNBQKBNR b KQkq - 0 1");

        expectedWhiteMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(12), new Coord(19), MoveType.PAWN_ATTACK, Figure.W_PAWN),
                        new MoveInfo(new Coord(12), new Coord(20), MoveType.USUAL_MOVE, Figure.W_PAWN),
                        new MoveInfo(new Coord(12), new Coord(28), MoveType.PAWN_LONG_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedWhiteMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.E2_IDX.ordinal())));

        expectedBlackMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(48), new Coord(40), MoveType.USUAL_MOVE, Figure.B_PAWN),
                        new MoveInfo(new Coord(48), new Coord(41), MoveType.PAWN_ATTACK, Figure.B_PAWN),
                        new MoveInfo(new Coord(48), new Coord(32), MoveType.PAWN_LONG_MOVE, Figure.B_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedBlackMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.A7_IDX.ordinal())));
    }

    @Test
    public void testPawnInCornersPosition() {
        FENBoard FENBoard = new FENBoard("rnbqkbnr/ppp1p1pp/8/3p1p2/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(28), new Coord(35), MoveType.PAWN_ATTACK, Figure.W_PAWN),
                        new MoveInfo(new Coord(28), new Coord(36), MoveType.USUAL_MOVE, Figure.W_PAWN),
                        new MoveInfo(new Coord(28), new Coord(37), MoveType.PAWN_ATTACK, Figure.W_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.E4_IDX.ordinal())));

        FENBoard = new FENBoard("1nbqkbn1/Pp2p1p1/7p/p5r1/7P/2p5/1PPPPPPp/RNBQKBN1 w - - 0 1");

        expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(48), new Coord(57), MoveType.PAWN_TO_FIGURE_ATTACK, Figure.W_PAWN),
                        new MoveInfo(new Coord(48), new Coord(56), MoveType.PAWN_TO_FIGURE, Figure.W_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.A7_IDX.ordinal())));

        expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(18), new Coord(9), MoveType.PAWN_ATTACK, Figure.B_PAWN),
                        new MoveInfo(new Coord(18), new Coord(11), MoveType.PAWN_ATTACK, Figure.B_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.C3_IDX.ordinal())));

        expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(31), new Coord(39), MoveType.USUAL_MOVE, Figure.W_PAWN),
                        new MoveInfo(new Coord(31), new Coord(38), MoveType.PAWN_ATTACK, Figure.W_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.H4_IDX.ordinal())));

        expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(32), new Coord(24), MoveType.USUAL_MOVE, Figure.B_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.A5_IDX.ordinal())));

        expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(15), new Coord(6), MoveType.PAWN_TO_FIGURE_ATTACK, Figure.B_PAWN),
                        new MoveInfo(new Coord(15), new Coord(7), MoveType.PAWN_TO_FIGURE, Figure.B_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.H2_IDX.ordinal())));

    }

    @Test
    public void testPawnEnPassant() {
        FENBoard FENBoard = new FENBoard("rnbqkbnr/ppp1pppp/8/2PpP3/8/8/PP1P1PPP/RNBQKBNR w KQkq d6 0 1");
        // Взятие на проходе атака вправо, для белых
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(34), new Coord(43), MoveType.PAWN_ON_GO_ATTACK, Figure.W_PAWN),
                        new MoveInfo(new Coord(34), new Coord(42), MoveType.USUAL_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.C5_IDX.ordinal())));

        // Взятие на проходе атака влево, для белых
        expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(36), new Coord(43), MoveType.PAWN_ON_GO_ATTACK, Figure.W_PAWN),
                        new MoveInfo(new Coord(36), new Coord(44), MoveType.USUAL_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.E5_IDX.ordinal())));

        FENBoard = new FENBoard("rnbqkbnr/ppp1p1pp/8/8/3pPp2/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        // Взятие на проходе атака влево, для чёрных
        expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(27), new Coord(20), MoveType.PAWN_ON_GO_ATTACK, Figure.B_PAWN),
                        new MoveInfo(new Coord(27), new Coord(19), MoveType.USUAL_MOVE, Figure.B_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.D4_IDX.ordinal())));
        // Взятие на проходе атака вправо, для чёрных
        expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(29), new Coord(20), MoveType.PAWN_ON_GO_ATTACK, Figure.B_PAWN),
                        new MoveInfo(new Coord(29), new Coord(21), MoveType.USUAL_MOVE, Figure.B_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(FENBoard, new Coord(BitUtils.BitIndex.F4_IDX.ordinal())));
    }
*/
}
