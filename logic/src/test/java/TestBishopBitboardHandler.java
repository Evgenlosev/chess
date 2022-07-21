import io.deeplay.logic.BitUtils;
import io.deeplay.api.BitboardHandler;
import io.deeplay.logic.ChessBoard;
import io.deeplay.model.Coord;
import io.deeplay.model.Figure;
import io.deeplay.model.MoveInfo;
import io.deeplay.model.MoveType;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestBishopBitboardHandler {

    @Test
    public void testBishopInMiddlePosition() {
        ChessBoard chessBoard = new ChessBoard("5rk1/K1P5/4B3/1p3P2/1Np5/8/3Q3R/8 w - - 0 1"); // белые
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(44), new Coord(51), MoveType.USUAL_MOVE, Figure.W_BISHOP),
                        new MoveInfo(new Coord(44), new Coord(58), MoveType.USUAL_MOVE, Figure.W_BISHOP),
                        new MoveInfo(new Coord(44), new Coord(53), MoveType.USUAL_MOVE, Figure.W_BISHOP),
                        new MoveInfo(new Coord(44), new Coord(62), MoveType.USUAL_ATTACK, Figure.W_BISHOP),
                        new MoveInfo(new Coord(44), new Coord(35), MoveType.USUAL_MOVE, Figure.W_BISHOP),
                        new MoveInfo(new Coord(44), new Coord(26), MoveType.USUAL_ATTACK, Figure.W_BISHOP))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getBishopMoves(chessBoard, new Coord(BitUtils.BitIndex.E6_IDX.ordinal())));
    }

    @Test
    public void testBishopInMiddlePositionClamped() { // слон зажат
        ChessBoard chessBoard = new ChessBoard("8/8/8/R3Q3/1k6/2b5/1r1p1K2/8 w - - 0 1"); // черные
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(18), new Coord(27), MoveType.USUAL_MOVE, Figure.B_BISHOP),
                        new MoveInfo(new Coord(18), new Coord(36), MoveType.USUAL_ATTACK, Figure.B_BISHOP))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getBishopMoves(chessBoard, new Coord(BitUtils.BitIndex.C3_IDX.ordinal())));
    }

    @Test
    public void testBishopsInCornersPosition() {
        ChessBoard chessBoard = new ChessBoard("7k/8/8/8/8/8/7b/B3K3 b - - 0 1"); // 0 - белый (в углу), 15 - черный (у одного края)
        Set<MoveInfo> expectedWhiteMoveInfoSet = Stream.of( // белый слон A1 - 0 клетка
                        new MoveInfo(new Coord(0), new Coord(9), MoveType.USUAL_MOVE, Figure.W_BISHOP),
                        new MoveInfo(new Coord(0), new Coord(18), MoveType.USUAL_MOVE, Figure.W_BISHOP),
                        new MoveInfo(new Coord(0), new Coord(27), MoveType.USUAL_MOVE, Figure.W_BISHOP),
                        new MoveInfo(new Coord(0), new Coord(36), MoveType.USUAL_MOVE, Figure.W_BISHOP),
                        new MoveInfo(new Coord(0), new Coord(45), MoveType.USUAL_MOVE, Figure.W_BISHOP),
                        new MoveInfo(new Coord(0), new Coord(54), MoveType.USUAL_MOVE, Figure.W_BISHOP),
                        new MoveInfo(new Coord(0), new Coord(63), MoveType.USUAL_ATTACK, Figure.W_BISHOP))
                .collect(Collectors.toSet());
        assertEquals(expectedWhiteMoveInfoSet, BitboardHandler.getBishopMoves(chessBoard, new Coord(BitUtils.BitIndex.A1_IDX.ordinal())));

        Set<MoveInfo> expectedBlackMoveInfoSet = Stream.of( // черный слон H2 - 15 клетка
                        new MoveInfo(new Coord(15), new Coord(6), MoveType.USUAL_MOVE, Figure.B_BISHOP),
                        new MoveInfo(new Coord(15), new Coord(22), MoveType.USUAL_MOVE, Figure.B_BISHOP),
                        new MoveInfo(new Coord(15), new Coord(29), MoveType.USUAL_MOVE, Figure.B_BISHOP),
                        new MoveInfo(new Coord(15), new Coord(36), MoveType.USUAL_MOVE, Figure.B_BISHOP),
                        new MoveInfo(new Coord(15), new Coord(43), MoveType.USUAL_MOVE, Figure.B_BISHOP),
                        new MoveInfo(new Coord(15), new Coord(50), MoveType.USUAL_MOVE, Figure.B_BISHOP),
                        new MoveInfo(new Coord(15), new Coord(57), MoveType.USUAL_MOVE, Figure.B_BISHOP))
                .collect(Collectors.toSet());

        assertEquals(expectedBlackMoveInfoSet, BitboardHandler.getBishopMoves(chessBoard, new Coord(BitUtils.BitIndex.H2_IDX.ordinal())));
    }
    
}
