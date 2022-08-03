package io.deeplay.core.api;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestSimpleLogic {

    @Test
    public void testIsMate() { // TODO: разбить тесты
        SimpleLogicAppeal simpleLogicAppeal = new SimpleLogic();
        String fenNotation = "8/8/3r4/k7/2r1r3/1Q6/3K4/3N3B w - - 0 1";

        assertFalse(simpleLogicAppeal.isMate(fenNotation)); // false т.к. короля может защитить королева
        System.out.println(simpleLogicAppeal.getMoves(fenNotation)); // TODO: assert множество ходов вместо sysout

        fenNotation = "8/8/3r4/k7/2r1r3/8/3K4/3N3B w - - 0 1"; // Тут королевы нет
        assertTrue(simpleLogicAppeal.isMate(fenNotation));
        System.out.println(simpleLogicAppeal.getMoves(fenNotation)); // Нету ходов

        fenNotation = "5B2/8/3r4/k7/2r1r3/8/3K4/3N3B w - - 0 1"; // Есть слон
        assertFalse(simpleLogicAppeal.isMate(fenNotation));
        System.out.println(simpleLogicAppeal.getMoves(fenNotation));

        fenNotation = "8/8/8/k7/2r1r3/3N1n2/3K4/3N3B w - - 0 1"; // Можно срубить коня
        assertFalse(simpleLogicAppeal.isMate(fenNotation));
        System.out.println(simpleLogicAppeal.getMoves(fenNotation));
    }

    /*
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
*/
}
