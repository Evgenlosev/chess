package board;

import io.deeplay.core.model.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.testng.AssertJUnit.assertEquals;

public class TestChessBoard {
    @Test
    public void zipFenTest() {
        String fen = "rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P2P/5N2/PPPP1PP1/RNBQK2R b KQkq - 0 1";
        ChessBoard board = new ChessBoard();
        assertEquals(board.zipFen(board.unzipFen(fen)), fen);
    }

    @Test
    public void updateBoardTest() {
        String fen = "rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 4 4";
        ChessBoard board = new ChessBoard(fen);
        MoveInfo moveinfo = new MoveInfo(new Coord(4, 0), new Coord(6, 0),
                MoveType.CASTLE_SHORT, Figure.W_KING);
//        MoveInfo moveinfo = new MoveInfo(new Coord(0, 1), new Coord(0, 3),
//                MoveType.PAWN_LONG_MOVE, Figure.W_PAWN);
        board.updateBoard(moveinfo);
        assertEquals("r n b q k - - r \n" +
                "p p p p - p p p \n" +
                "- - - - - n - - \n" +
                "- - b - p - - - \n" +
                "- - B - P - - - \n" +
                "- - - - - N - - \n" +
                "P P P P - P P P \n" +
                "R N B Q - R K - \n" , board.toString());
    }

}
