package logic;

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

// Мегаполезная штука: https://www.dailychess.com/chess/chess-fen-viewer.php - можно расставлять фигуры + есть FEN
public class TestRookBitboardHandler {
    // TODO: связанная за своим королем ладья должна ходить только по линии атаки связывающей фигуры
    //  по сути надо сделать && битборда атаки с битбородом атаки фигуры, угрожающей королю

    // TODO: тест на невозможность походить из изначальной позиции
    // TODO: тест когда фигура связана и не может ходить
    // TODO: тест когда фигура связана и не может ходить, но есть возможность срубить НЕ связывающую фигуру
    // TODO: тест когда фигура связана и может срубить связывающую фигуру

    @Test
    public void testRookInMiddlePosition() {
        ChessBoard chessBoard = new ChessBoard("8/K1P1Rp2/8/8/8/4k3/8/8 b - - 0 1"); // белые
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(52), new Coord(60), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(52), new Coord(51), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(52), new Coord(28), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(52), new Coord(44), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(52), new Coord(36), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(52), new Coord(20), MoveType.USUAL_ATTACK, Figure.W_ROOK),
                        new MoveInfo(new Coord(52), new Coord(53), MoveType.USUAL_ATTACK, Figure.W_ROOK))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getRookMoves(chessBoard, new Coord(BitUtils.BitIndex.E7_IDX.ordinal())));
    }

    @Test
    public void testRookInMiddlePositionClamped() { // ладья зажата
        ChessBoard chessBoard = new ChessBoard("8/1k2Rrn1/5Q2/8/8/8/5K2/8 b - - 0 1"); // черные
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(53), new Coord(61), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(53), new Coord(45), MoveType.USUAL_ATTACK, Figure.B_ROOK),
                        new MoveInfo(new Coord(53), new Coord(52), MoveType.USUAL_ATTACK, Figure.B_ROOK))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, BitboardHandler.getRookMoves(chessBoard, new Coord(BitUtils.BitIndex.F7_IDX.ordinal())));
    }

    @Test
    public void testRooksInCornersPosition() {
        ChessBoard chessBoard = new ChessBoard("R6k/8/8/8/8/K6r/8/8 b - - 0 1"); // 56 - белый (в углу), 23 - черный (у одного края)
        Set<MoveInfo> expectedWhiteMoveInfoSet = Stream.of( // белая ладья A8 - 56 клетка
                        new MoveInfo(new Coord(56), new Coord(61), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(24), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(32), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(62), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(59), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(60), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(48), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(57), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(58), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(40), MoveType.USUAL_MOVE, Figure.W_ROOK),
                        new MoveInfo(new Coord(56), new Coord(63), MoveType.USUAL_ATTACK, Figure.W_ROOK))
                .collect(Collectors.toSet());
        assertEquals(expectedWhiteMoveInfoSet, BitboardHandler.getRookMoves(chessBoard, new Coord(BitUtils.BitIndex.A8_IDX.ordinal())));

        Set<MoveInfo> expectedBlackMoveInfoSet = Stream.of( // черная ладья H3 - 23 клетка
                        new MoveInfo(new Coord(23), new Coord(20), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(7), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(47), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(55), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(15), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(39), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(31), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(22), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(21), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(19), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(18), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(17), MoveType.USUAL_MOVE, Figure.B_ROOK),
                        new MoveInfo(new Coord(23), new Coord(16), MoveType.USUAL_ATTACK, Figure.B_ROOK))
                .collect(Collectors.toSet());

        assertEquals(expectedBlackMoveInfoSet, BitboardHandler.getRookMoves(chessBoard, new Coord(BitUtils.BitIndex.H3_IDX.ordinal())));
    }

}
