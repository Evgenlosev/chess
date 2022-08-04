package io.deeplay.core.console;

import io.deeplay.core.model.ChessBoard;
import io.deeplay.core.model.Figure;

import java.util.Map;

public class BoardDrawer {
    static final String blackCell = "\033[48;5;116m\033[38;5;0m ";
    static String whiteCell = "\033[48;5;230m\033[38;5;0m ";
    static Map<Figure, String> whiteFigures = Map.of(
            Figure.W_PAWN, "\u265F",
            Figure.W_ROOK, "\u265C",
            Figure.W_KNIGHT, "\u265E",
            Figure.W_BISHOP, "\u265D",
            Figure.W_KING, "\u265A",
            Figure.W_QUEEN, "\u265B"
    );
    static String columns = "   a      b      c      d      e      f      g      h  ";
    public static void draw(final String fen) {
        // Очистить экран
        System.out.print("\033[H\033[J");
        ChessBoard board = new ChessBoard(fen);
        String unzipBoard = board.unzipFen(board.getFEN()).replace("/", "");
        unzipBoard = unzipBoard.replace("p", "\u265F");
        unzipBoard = unzipBoard.replace("r", "\u265C");
        unzipBoard = unzipBoard.replace("n", "\u265E");
        unzipBoard = unzipBoard.replace("b", "\u265D");
        unzipBoard = unzipBoard.replace("q", "\u265B");
        unzipBoard = unzipBoard.replace("k", "\u265A");
        unzipBoard = unzipBoard.replace("P", "\u2659");
        unzipBoard = unzipBoard.replace("R", "\u2656");
        unzipBoard = unzipBoard.replace("N", "\u2658");
        unzipBoard = unzipBoard.replace("B", "\u2657");
        unzipBoard = unzipBoard.replace("Q", "\u2655");
        unzipBoard = unzipBoard.replace("K", "\u2654");
        unzipBoard = unzipBoard.replace("1", " ");

        // Перенос курсора в позицию (0:0)
        System.out.print("\033[0;0H");
        for (int i = 0; i < 8; i++) {
            for (int l = 0; l < 3; l++) {
                if (l == 1) {
                    System.out.print(8 - i);
                    System.out.print(" ");
                } else {
                    System.out.print("  ");
                }
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 3; k++) {
                        boolean isWhiteCell = i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1;
                        if (l != 1) {
                            if (isWhiteCell) {
                                System.out.print(blackCell.repeat(7));
                            } else {
                                System.out.print(whiteCell.repeat(7));
                            }
                            break;
                        }
                        if (k == 1) {
                            if (isWhiteCell) {
                                System.out.print(blackCell + unzipBoard.charAt(i * 8 + j) + blackCell);
                            } else {
                                System.out.print(whiteCell + unzipBoard.charAt(i * 8 + j) + whiteCell);
                            }
                        } else {
                            if (isWhiteCell) {
                                System.out.print(blackCell.repeat(2));
                            } else {
                                System.out.print(whiteCell.repeat(2));
                            }
                        }
                    }
                }
                System.out.println("\033[m");
            }
        }
        System.out.println("  " + columns);
        System.out.println("\033[m");
    }
}
