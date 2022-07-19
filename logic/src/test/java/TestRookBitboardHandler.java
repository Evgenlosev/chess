import io.deeplay.BitUtils;
import io.deeplay.BitboardHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRookBitboardHandler {
    // TODO: связанная за своим королем ладья должна ходить только по линии атаки связывающей фигуры
    //  по сути надо сделать && битборда атаки с битбородом атаки фигуры, угрожающей королю

    private long ALL_MASK = 0xFFFFFFFFFFFFFFFFL;
    private long EMPTY_MASK = 0L;

    @Test
    public void testRookInCornerPositions() {
        long occupied =     0b0010000000000000000000000000000000010001000000000010000100000000L;
        int rookPosition = 8;

        /*
        8| 0 0 0 0 0 1 0 0
        7| 0 0 0 0 0 0 0 0
        6| 0 0 0 0 0 0 0 0
        5| 0 0 0 0 0 0 0 0
        4| 0 0 0 1 1 0 0 0
        3| 0 0 0 0 0 0 0 0
        2|[1] 0 0 0 1 0 0 0
        1| 0 0 0 0 0 0 0 0
           _______________
           a b c d e f g h
         */

        long expectedBitboard = 0b0000000000000000000000000000000000000001000000010011111000000001L;
        assertEquals(BitUtils.getBitboardAsBinaryString(expectedBitboard),
                BitUtils.getBitboardAsBinaryString(BitboardHandler.getRookMoves(rookPosition, occupied)));
    }

    @Test
    public void testRookInMiddlePosition() {
        long occupied =     0b0010000000000000000000000000000000011000000000000001000100000000L;
        int rookPosition = 28;

        /*
        8| 0 0 0 0 0 1 0 0
        7| 0 0 0 0 0 0 0 0
        6| 0 0 0 0 0 0 0 0
        5| 0 0 0 0 0 0 0 0
        4| 0 0 0 1[1]0 0 0
        3| 0 0 0 0 0 0 0 0
        2| 1 0 0 0 1 0 0 0
        1| 0 0 0 0 0 0 0 0
           _______________
           a b c d e f g h
         */

        long expectedBitboard = 0b0001000000010000000100000001000011101000000100000001000000000000L;
        assertEquals(BitUtils.getBitboardAsBinaryString(expectedBitboard),
                BitUtils.getBitboardAsBinaryString(BitboardHandler.getRookMoves(rookPosition, occupied)));
    }

}
