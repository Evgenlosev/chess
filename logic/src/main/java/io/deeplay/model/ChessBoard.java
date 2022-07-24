package io.deeplay.model;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.Map.entry;

public class ChessBoard {
    public static final int boardSize = 8;
    private BoardCell[][] board = new BoardCell[boardSize][boardSize];
    Logger logger = (Logger) LoggerFactory.getLogger(ChessBoard.class);
    private ChessBoard previousChessBoard = null;

    Map<String, Figure> symbolsToFigure = Map.ofEntries(
            entry("p", Figure.B_PAWN),
            entry("r", Figure.B_ROOK),
            entry("n", Figure.B_KNIGHT),
            entry("b", Figure.B_BISHOP),
            entry("q", Figure.B_QUEEN),
            entry("k", Figure.B_KING),
            entry("P", Figure.W_PAWN),
            entry("R", Figure.W_ROOK),
            entry("N", Figure.W_KNIGHT),
            entry("B", Figure.W_BISHOP),
            entry("Q", Figure.W_QUEEN),
            entry("K", Figure.W_KING),
            entry("1", Figure.NONE)
    );
    Map<Figure, String> figureToSymbol = Map.ofEntries(
            entry(Figure.B_PAWN, "p"),
            entry(Figure.B_ROOK, "r"),
            entry(Figure.B_KNIGHT, "n"),
            entry(Figure.B_BISHOP, "b"),
            entry(Figure.B_QUEEN, "q"),
            entry(Figure.B_KING, "k"),
            entry(Figure.W_PAWN, "P"),
            entry(Figure.W_ROOK, "R"),
            entry(Figure.W_KNIGHT, "N"),
            entry(Figure.W_BISHOP, "B"),
            entry(Figure.W_QUEEN, "Q"),
            entry(Figure.W_KING, "K"),
            entry(Figure.NONE, "-")
    );

    /**
     * Default constructor to create default board;
     */
    public ChessBoard() {
        this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    /**
     * Constructor to create custom start board from FEN string
     * Options like long pawn move, move number, will be ignored.
     * @param fen FEN string.
     */
    public ChessBoard(final String fen) {
//        throw new RuntimeException("Это ещё не было реализовано )-:");
        if (fen.length() - fen.replace("/", "").length() != 7) {
            throw new RuntimeException("Wrong FEN string");
        }
        String[] splitFiguresFromProperties = fen.split(" ", 2);
        String unzippedFenWithoutProperties = unzipFen(splitFiguresFromProperties[0]).replace("/", "");
        for (int i = 0; i < unzippedFenWithoutProperties.length(); i++) {
            board[7 - i / 8][7 - i % 8] = new BoardCell(
                    symbolsToFigure.get(String.valueOf(unzippedFenWithoutProperties.charAt(i))));
        }
    }

    public String unzipFen(String fen) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fen.length(); i++) {
            if (String.valueOf(fen.charAt(i)).matches("[1-8]")) {
                sb.append("1".repeat(Character.getNumericValue(fen.charAt(i))));
            } else {
                sb.append(fen.charAt(i));
            }
        }
        return sb.toString();
    }

    public String zipFen(final String fen) {
        StringBuilder sb = new StringBuilder();
        int emptyCellCounter = 0;

        for (int i = 0; i < fen.length(); i++) {
            if (fen.charAt(i) == '1') {
                emptyCellCounter++;
                continue;
            }
            if (emptyCellCounter > 0) {
                sb.append(emptyCellCounter);
                emptyCellCounter = 0;
            }
            sb.append(fen.charAt(i));
        }
        if (emptyCellCounter > 0) {
            sb.append(emptyCellCounter);
        }
        return sb.toString();
    }

    public void updateBoard(MoveInfo moveInfo) {

        try {
            previousChessBoard = (ChessBoard) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        // TODO:: Сделать перемещение фигуры.
    }

    public String getFEN() {
        return "88005553535";
        // TODO:: Получить из массива строку фен, с указанием очередности хода, номером хода, количеством ходов без взятия фигур.
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

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                sb.append(figureToSymbol.get(board[i][j].getFigure())).append(" ");
            }
            sb.append("\n");
        }

        return sb.reverse().toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
