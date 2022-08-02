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

    // TODO: Проверка в fen на то что 2 короля ближе 1 клетки
    // TODO: тесты (не успел)
    @Test
    public void testPawns() {
        SimpleLogicAppeal simpleLogicAppeal = new SimpleLogic();
        Set<MoveInfo> moveInfoSet = simpleLogicAppeal.getMoves("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(34), new Coord(43), MoveType.PAWN_ON_GO_ATTACK, Figure.W_PAWN),
                        new MoveInfo(new Coord(34), new Coord(42), MoveType.USUAL_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, moveInfoSet); // провальный, но выводит верно, должно быть 20 ходов, надо исправить expectedMoveInfoSet
    }

    @Test
    public void testKings() {
        SimpleLogicAppeal simpleLogicAppeal = new SimpleLogic();
        Set<MoveInfo> moveInfoSet = simpleLogicAppeal.getMoves("8/8/8/8/8/2K1k3/8/8 w - - 0 1");
        Set<MoveInfo> expectedMoveInfoSet = Stream.of(
                        new MoveInfo(new Coord(34), new Coord(43), MoveType.PAWN_ON_GO_ATTACK, Figure.W_PAWN),
                        new MoveInfo(new Coord(34), new Coord(42), MoveType.USUAL_MOVE, Figure.W_PAWN))
                .collect(Collectors.toSet());

        assertEquals(expectedMoveInfoSet, moveInfoSet); // TODO: у обоих королей должно быть только 5 клеток для хода
    }


}
