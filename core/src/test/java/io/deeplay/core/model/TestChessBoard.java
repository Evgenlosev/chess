package io.deeplay.core.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        board.updateBoard(moveinfo);
        assertEquals("r n b q k - - r \n" +
                "p p p p - p p p \n" +
                "- - - - - n - - \n" +
                "- - b - p - - - \n" +
                "- - B - P - - - \n" +
                "- - - - - N - - \n" +
                "P P P P - P P P \n" +
                "R N B Q - R K - \n" , board.toString());

        moveinfo = new MoveInfo(new Coord(0, 6), new Coord(0, 4),
                MoveType.PAWN_LONG_MOVE, Figure.B_PAWN);
        board.updateBoard(moveinfo);
        assertEquals("r n b q k - - r \n" +
                "- p p p - p p p \n" +
                "- - - - - n - - \n" +
                "p - b - p - - - \n" +
                "- - B - P - - - \n" +
                "- - - - - N - - \n" +
                "P P P P - P P P \n" +
                "R N B Q - R K - \n", board.toString());
        moveinfo = new MoveInfo(new Coord(0, 1), new Coord(0, 2),
                MoveType.USUAL_MOVE, Figure.W_PAWN);
        board.updateBoard(moveinfo);
        assertEquals("r n b q k - - r \n" +
                "- p p p - p p p \n" +
                "- - - - - n - - \n" +
                "p - b - p - - - \n" +
                "- - B - P - - - \n" +
                "P - - - - N - - \n" +
                "- P P P - P P P \n" +
                "R N B Q - R K - \n", board.toString());

        fen = "r3kbnr/pp2pppp/2nqb3/2pp4/2PP1B2/1QN5/PP2PPPP/R3KBNR w KQkq - 6 6";
        board = new ChessBoard(fen);
        moveinfo = new MoveInfo(new Coord(3, 3), new Coord(2, 4),
                MoveType.PAWN_ATTACK, Figure.W_PAWN);
        board.updateBoard(moveinfo);
        assertEquals("r - - - k b n r \n" +
                "p p - - p p p p \n" +
                "- - n q b - - - \n" +
                "- - P p - - - - \n" +
                "- - P - - B - - \n" +
                "- Q N - - - - - \n" +
                "P P - - P P P P \n" +
                "R - - - K B N R \n", board.toString());

        moveinfo = new MoveInfo(new Coord(1, 6), new Coord(1, 4),
                MoveType.PAWN_LONG_MOVE, Figure.B_PAWN);
        board.updateBoard(moveinfo);
        assertEquals("r - - - k b n r \n" +
                "p - - - p p p p \n" +
                "- - n q b - - - \n" +
                "- p P p - - - - \n" +
                "- - P - - B - - \n" +
                "- Q N - - - - - \n" +
                "P P - - P P P P \n" +
                "R - - - K B N R \n", board.toString());

        moveinfo = new MoveInfo(new Coord(2, 4), new Coord(1, 5),
                MoveType.PAWN_ON_GO_ATTACK, Figure.W_PAWN);
        board.updateBoard(moveinfo);
        assertEquals("r - - - k b n r \n" +
                "p - - - p p p p \n" +
                "- P n q b - - - \n" +
                "- - - p - - - - \n" +
                "- - P - - B - - \n" +
                "- Q N - - - - - \n" +
                "P P - - P P P P \n" +
                "R - - - K B N R \n", board.toString());

        moveinfo = new MoveInfo(new Coord(4, 7), new Coord(2, 7),
                MoveType.CASTLE_LONG, Figure.B_KING);
        board.updateBoard(moveinfo);
        assertEquals("- - k r - b n r \n" +
                "p - - - p p p p \n" +
                "- P n q b - - - \n" +
                "- - - p - - - - \n" +
                "- - P - - B - - \n" +
                "- Q N - - - - - \n" +
                "P P - - P P P P \n" +
                "R - - - K B N R \n", board.toString());
    }


}
