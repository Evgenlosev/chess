import io.deeplay.BitUtils;
import io.deeplay.BitboardHandler;
import io.deeplay.ChessBoard;
import io.deeplay.logic.board.Coord;
import io.deeplay.logic.board.Figure;
import io.deeplay.logic.board.MoveInfo;
import io.deeplay.logic.board.MoveType;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestBishopBitboardHandler {

    @Test
    public void testBishopInMiddlePosition() {
        ChessBoard chessBoard = new ChessBoard("8/K1P1Rp2/8/8/8/4k3/8/8 b - - 0 1"); // белые
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(52), new Coord(60), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(52), new Coord(51), MoveType.USUAL_MOVE, Figure.W_ROOK))
                .collect(Collectors.toSet());


        assertEquals(expectedMoveInfoSet, BitboardHandler.getBishopMoves(chessBoard, new Coord(BitUtils.BitIndex.E7_IDX.ordinal())));
    }

    @Test
    public void testBishopInMiddlePositionClamped() { // ладья зажата
        ChessBoard chessBoard = new ChessBoard("8/1k2Rrn1/5Q2/8/8/8/5K2/8 b - - 0 1"); // черные
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(53), new Coord(61), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(53), new Coord(45), MoveType.USUAL_ATTACK, Figure.B_ROOK))
                .collect(Collectors.toSet());


        assertEquals(expectedMoveInfoSet, BitboardHandler.getBishopMoves(chessBoard, new Coord(BitUtils.BitIndex.F7_IDX.ordinal())));
    }

    @Test
    public void testBishopsInCornersPosition() {
        ChessBoard chessBoard = new ChessBoard("R6k/8/8/8/8/K6r/8/8 b - - 0 1"); // 56 - белый (в углу), 23 - черный (у одного края)
        Set<MoveInfo> expectedWhiteMoveInfoSet = Stream.of( // белая ладья A8 - 56 клетка
                        new MoveInfo(new Coord(56), new Coord(61), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(24), MoveType.USUAL_MOVE, Figure.W_ROOK))
                .collect(Collectors.toSet());
        assertEquals(expectedWhiteMoveInfoSet, BitboardHandler.getBishopMoves(chessBoard, new Coord(BitUtils.BitIndex.A8_IDX.ordinal())));

        Set<MoveInfo> expectedBlackMoveInfoSet = Stream.of( // черная ладья H3 - 23 клетка
                        new MoveInfo(new Coord(23), new Coord(20), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(7), MoveType.USUAL_MOVE, Figure.B_ROOK))
                .collect(Collectors.toSet());

        assertEquals(expectedBlackMoveInfoSet, BitboardHandler.getBishopMoves(chessBoard, new Coord(BitUtils.BitIndex.H3_IDX.ordinal())));
    }
    
}
