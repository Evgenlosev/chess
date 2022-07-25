package io.deeplay.board;

import io.deeplay.model.ChessBoard;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestChessBoard {
    @Test
    public void zipFenTest() {
        String fen = "rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P2P/5N2/PPPP1PP1/RNBQK2R b KQkq - 0 1";
        ChessBoard board = new ChessBoard();
        assertEquals(board.zipFen(board.unzipFen(fen)), fen);
    }
}
