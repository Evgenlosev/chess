import io.deeplay.logic.BitUtils;
import io.deeplay.logic.BitboardHandler;
import io.deeplay.logic.ChessBoard;
import io.deeplay.logic.board.Coord;
import io.deeplay.logic.board.Figure;
import io.deeplay.logic.board.MoveInfo;
import io.deeplay.logic.board.MoveType;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestPawnBitboardHandler {

    // en passant у краев доски за обе стороны
    // с обоих краев доски, за обе стороны
    // превращение с взятием/без для обеих сторон
    @Test
    public void testPawnAtStartPositionClassic() {
        ChessBoard chessBoard = new ChessBoard("rnbqkbnr/ppp1p1pp/8/3p1p2/4P3/1P6/P1PP1PPP/RNBQKBNR b KQkq - 0 1");
        Set<MoveInfo> expectedWhiteMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(11), new Coord(19), MoveType.USUAL_MOVE, Figure.W_PAWN),
                        new MoveInfo(new Coord(11), new Coord(27), MoveType.PAWN_LONG_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedWhiteMoveInfoSet, BitboardHandler.getPawnMoves(chessBoard, new Coord(BitUtils.BitIndex.D2_IDX.ordinal())));

        Set<MoveInfo> expectedBlackMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(37), new Coord(28), MoveType.PAWN_ATTACK, Figure.B_PAWN),
                        new MoveInfo(new Coord(37), new Coord(29), MoveType.USUAL_MOVE, Figure.B_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedBlackMoveInfoSet, BitboardHandler.getPawnMoves(chessBoard, new Coord(BitUtils.BitIndex.F5_IDX.ordinal())));

        expectedBlackMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(55), new Coord(47), MoveType.USUAL_MOVE, Figure.B_PAWN),
                        new MoveInfo(new Coord(55), new Coord(39), MoveType.PAWN_LONG_MOVE, Figure.B_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedBlackMoveInfoSet, BitboardHandler.getPawnMoves(chessBoard, new Coord(BitUtils.BitIndex.H7_IDX.ordinal())));

        expectedWhiteMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(17), new Coord(25), MoveType.USUAL_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedWhiteMoveInfoSet, BitboardHandler.getPawnMoves(chessBoard, new Coord(BitUtils.BitIndex.B3_IDX.ordinal())));

        chessBoard = new ChessBoard("rnbqkbnr/ppp1pppp/1P6/8/8/3p4/P1PPPPPP/RNBQKBNR b KQkq - 0 1");

        expectedWhiteMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(12), new Coord(19), MoveType.PAWN_ATTACK, Figure.W_PAWN),
                        new MoveInfo(new Coord(12), new Coord(20), MoveType.USUAL_MOVE, Figure.W_PAWN),
                        new MoveInfo(new Coord(12), new Coord(28), MoveType.PAWN_LONG_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedWhiteMoveInfoSet, BitboardHandler.getPawnMoves(chessBoard, new Coord(BitUtils.BitIndex.E2_IDX.ordinal())));

        expectedBlackMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(48), new Coord(40), MoveType.USUAL_MOVE, Figure.B_PAWN),
                        new MoveInfo(new Coord(48), new Coord(41), MoveType.PAWN_ATTACK, Figure.B_PAWN),
                        new MoveInfo(new Coord(48), new Coord(32), MoveType.PAWN_LONG_MOVE, Figure.B_PAWN))
                .collect(Collectors.toSet());
        assertEquals(expectedBlackMoveInfoSet, BitboardHandler.getPawnMoves(chessBoard, new Coord(BitUtils.BitIndex.A7_IDX.ordinal())));
    }

    @Test
    public void testPawnInMiddlePosition() {
        ChessBoard chessBoard = new ChessBoard("rnbqkbnr/ppp1p1pp/8/3p1p2/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(28), new Coord(35), MoveType.PAWN_ATTACK, Figure.W_PAWN),
                        new MoveInfo(new Coord(28), new Coord(36), MoveType.USUAL_MOVE, Figure.W_PAWN),
                        new MoveInfo(new Coord(28), new Coord(37), MoveType.PAWN_ATTACK, Figure.W_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getPawnMoves(chessBoard, new Coord(BitUtils.BitIndex.E4_IDX.ordinal())));
    }

}
