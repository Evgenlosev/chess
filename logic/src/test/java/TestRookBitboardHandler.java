import io.deeplay.BitUtils;
import io.deeplay.BitboardHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRookBitboardHandler {
    // TODO: "закрепленная" за своим королем ладья должна ходить только по линии атаки "зажимающей" фигуры
    //  по сути надо сделать && битборда атаки с битбородом атаки фигуры, угрожающей королю

    private long ALL_MASK = 0xFFFFFFFFFFFFFFFFL;
    private long EMPTY_MASK = 0L;

    @Test
    public void testRookInCornerPositions() {
        long occupied =     0b0010000000000000000000000000000000010001000000000010000100000000L;
        long rookBitboard = 0b0000000000000000000000000000000000000000000000000000000100000000L;
        BitUtils.printBitboard(occupied);

//        BitUtils.printBitboard(BitboardHandler.getRookMoves(8, occupied));
        assertEquals(8, BitUtils.bitCount(BitboardHandler.getRookMoves(8, occupied)));
    }

    @Test
    public void testRookInMiddlePosition() {
        long occupied =     0b0010000000000000000000000000000000011000000000000001000100000000L;
        long rookBitboard = 0b0000000000000000000000000000000000000000000000000000000100000000L;
        BitUtils.printBitboard(occupied);

//        BitUtils.printBitboard(BitboardHandler.getRookMoves(28, occupied));
        assertEquals(10, BitUtils.bitCount(BitboardHandler.getRookMoves(28, occupied)));
    }

}
