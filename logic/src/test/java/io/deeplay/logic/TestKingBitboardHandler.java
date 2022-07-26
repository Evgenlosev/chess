package io.deeplay.logic;


import io.deeplay.core.model.Coord;
import io.deeplay.core.model.Figure;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.MoveType;
import io.deeplay.logic.api.BitboardHandler;
import io.deeplay.logic.logic.BitUtils;
import io.deeplay.logic.logic.ChessBoard;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestKingBitboardHandler {
    // TODO: тест на невозможность королю срубить фигуру под защитой
    // TODO: тест на "дуэль королей" (они не могут подойти ближе 1 клетки)
    // TODO: тест на невозможность пройти линию атаки фигуры, которая связана
    // TODO: тест на невозможность пройти линию атаки
    // TODO: тест на рокировку (с 2 сторон, невозможность совершить рокировку (т.к. потеряно право)
    // TODO: тесты с фигурами на пути рокировки(возможности рокировки быть не должно)
    @Test
    public void getKingMovesTest() {
        /*
         * Check possible E1 king's moves in default position
         */
        ChessBoard chessBoard = new ChessBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Set<MoveInfo> expectedMoveInfoSet = new HashSet<>();
        assertEquals(expectedMoveInfoSet, BitboardHandler.getKingMoves(chessBoard, new Coord(BitUtils.BitIndex.E1_IDX.ordinal())));

        /*
         * Check possible E1 king's moves after e4e5
         */
        chessBoard = new ChessBoard("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 1");
        expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(4, 0), new Coord(4, 1), MoveType.USUAL_MOVE, Figure.W_KING))
                .collect(Collectors.toSet());
        assertEquals(expectedMoveInfoSet, BitboardHandler.getKingMoves(chessBoard, new Coord(BitUtils.BitIndex.E1_IDX.ordinal())));

        /*
         * Check possible G8 king's moves after 1. e4 e5 2. Nf3 d5 3. exd5 e4 4. Qe2 Nf6 5. d3 Qxd5 6. Nbd2 Nc6
         * 7. dxe4 Qh5 8. Qb5 Bc5 9. e5 Nd7 10. e6 fxe6 11. Nb3 a6 12. Qc4 Be7 13. Be2 Nb6 14. Qe4 Qf5
         * 15. Qxf5 exf5 16. O-O O-O 17. Bf4 Nd5 18. Bc4 Be6 19. Bg5
         */
        chessBoard = new ChessBoard("r4rk1/1pp1b1pp/p1n1b3/3n1pB1/2B5/1N3N2/PPP2PPP/R4RK1 b - - 7 19");
        expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(6, 7), new Coord(7, 7), MoveType.USUAL_MOVE, Figure.B_KING),
                        new MoveInfo(new Coord(6, 7), new Coord(5, 6), MoveType.USUAL_MOVE, Figure.B_KING))
                .collect(Collectors.toSet());
        assertEquals(expectedMoveInfoSet, BitboardHandler.getKingMoves(chessBoard, new Coord(BitUtils.BitIndex.G8_IDX.ordinal())));

    }
}
