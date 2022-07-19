package io.deeplay.logic.board;


public class ChessBoard {

    private BoardCell[][] board = new BoardCell[10][10];

    /**
     * Default constructor to create default board;
     */
    public ChessBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i < 1 || i > 8 || j < 1 || j > 8) {
                    board[i][j] = new BoardCell(true, true, Figure.NONE);
                }
                if (i == 1 || j == 1 || i == 8 || j == 8) {
                    board[i][j] = new BoardCell(false, true, Figure.NONE);
                } else {
                    board[i][j] = new BoardCell(false, false, Figure.NONE);
                }
            }
        }
        board[1][1] = new BoardCell(false, true, Figure.W_ROOK);
        board[1][2] = new BoardCell(false, true, Figure.W_KNIGHT);
        board[1][3] = new BoardCell(false, true, Figure.W_BISHOP);
        board[1][4] = new BoardCell(false, true, Figure.W_QUEEN);
        board[1][5] = new BoardCell(false, true, Figure.W_KING);
        board[1][6] = new BoardCell(false, true, Figure.W_BISHOP);
        board[1][7] = new BoardCell(false, true, Figure.W_KNIGHT);
        board[1][8] = new BoardCell(false, true, Figure.W_ROOK);

        board[2][1] = new BoardCell(false, true, Figure.W_PAWN);
        board[2][2] = new BoardCell(false, false, Figure.W_PAWN);
        board[2][3] = new BoardCell(false, false, Figure.W_PAWN);
        board[2][4] = new BoardCell(false, false, Figure.W_PAWN);
        board[2][5] = new BoardCell(false, false, Figure.W_PAWN);
        board[2][6] = new BoardCell(false, false, Figure.W_PAWN);
        board[2][7] = new BoardCell(false, false, Figure.W_PAWN);
        board[2][8] = new BoardCell(false, true, Figure.W_PAWN);

        board[7][1] = new BoardCell(false, true, Figure.B_PAWN);
        board[7][2] = new BoardCell(false, false, Figure.B_PAWN);
        board[7][3] = new BoardCell(false, false, Figure.B_PAWN);
        board[7][4] = new BoardCell(false, false, Figure.B_PAWN);
        board[7][5] = new BoardCell(false, false, Figure.B_PAWN);
        board[7][6] = new BoardCell(false, false, Figure.B_PAWN);
        board[7][7] = new BoardCell(false, false, Figure.B_PAWN);
        board[7][8] = new BoardCell(false, true, Figure.B_PAWN);

        board[8][1] = new BoardCell(false, true, Figure.B_ROOK);
        board[8][2] = new BoardCell(false, true, Figure.B_KNIGHT);
        board[8][3] = new BoardCell(false, true, Figure.B_BISHOP);
        board[8][4] = new BoardCell(false, true, Figure.B_QUEEN);
        board[8][5] = new BoardCell(false, true, Figure.B_KING);
        board[8][6] = new BoardCell(false, true, Figure.B_BISHOP);
        board[8][7] = new BoardCell(false, true, Figure.B_KNIGHT);
        board[8][8] = new BoardCell(false, true, Figure.B_ROOK);
    }

    public BoardCell[][] getBoard() {
        return board;
    }

    public void setBoard(final BoardCell[][] board) {
        this.board = board;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                switch (board[i][j].getFigure()) {
                    case NONE:
                        sb.append("  ");
                        break;
                    case B_KING:
                        sb.append("K ");
                        break;
                    case B_BISHOP:
                        sb.append("B ");
                        break;
                    case B_KNIGHT:
                        sb.append("N ");
                        break;
                    case B_ROOK:
                        sb.append("R ");
                        break;
                    case B_PAWN:
                        sb.append("P ");
                        break;
                    case B_QUEEN:
                        sb.append("Q ");
                        break;
                    case W_KING:
                        sb.append("K ");
                        break;
                    case W_BISHOP:
                        sb.append("B ");
                        break;
                    case W_KNIGHT:
                        sb.append("N ");
                        break;
                    case W_ROOK:
                        sb.append("R ");
                        break;
                    case W_PAWN:
                        sb.append("P ");
                        break;
                    case W_QUEEN:
                        sb.append("Q ");
                        break;
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
