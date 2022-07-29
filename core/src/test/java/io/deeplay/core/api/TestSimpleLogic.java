package io.deeplay.core.api;

import io.deeplay.core.model.Coord;
import io.deeplay.core.model.Figure;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.MoveType;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestSimpleLogic {

    // TODO: тесты (не успел)
    @Test
    public void testPawns() {
        SimpleLogicAppeal simpleLogicAppeal = new SimpleLogic();
        Set<MoveInfo> moveInfoSet = simpleLogicAppeal.getMoves("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(34), new Coord(43), MoveType.PAWN_ON_GO_ATTACK, Figure.W_PAWN),
                        new MoveInfo(new Coord(34), new Coord(42), MoveType.USUAL_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, moveInfoSet); // провальный, но выводит верно, должно быть 20 ходов, надо исправить expectedMoveInfoSet
    }

}
