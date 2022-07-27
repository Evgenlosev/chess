package io.deeplay.core.model;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public class ChessBoard  implements Cloneable{
    public static final int boardSize = 8;
    private BoardCell[][] board = new BoardCell[boardSize][boardSize];
    Logger logger = (Logger) LoggerFactory.getLogger(ChessBoard.class);
    private ChessBoard previousChessBoard = null;
    private MoveInfo moveInfo = null;
    private final Set<Figure> blackFigures = Set.of(
            Figure.B_PAWN,
            Figure.B_ROOK,
            Figure.B_KNIGHT,
            Figure.B_BISHOP,
            Figure.B_KING,
            Figure.B_QUEEN
            );
    private final Set<Figure> whiteFigures = Set.of(
            Figure.W_PAWN,
            Figure.W_ROOK,
            Figure.W_KNIGHT,
            Figure.W_BISHOP,
            Figure.W_KING,
            Figure.W_QUEEN
            );

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
            board[7 - i / 8][i % 8] = new BoardCell(
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

    public void updateBoard(final MoveInfo moveInfo) {
        // TODO:: сделать проверку на цвет ходящего и обновлять его после каждого хода
        try {
            previousChessBoard = (ChessBoard) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        this.moveInfo = moveInfo;
        // TODO:: Сделать перемещение фигуры.

        Coord from = moveInfo.getCellFrom();
        Coord to = moveInfo.getCellTo();

        switch (moveInfo.getMoveType()) {
            case USUAL_MOVE:
                if (board[to.getRow()][to.getColumn()].getFigure() == Figure.NONE) {
                    board[to.getRow()][to.getColumn()].setFigure(moveInfo.getFigure());
                } else {
                    throw new RuntimeException("Usual move tries to get occupied cell");
                }
                break;
            case USUAL_ATTACK:
                if (whiteFigures.contains(board[from.getRow()][from.getColumn()].getFigure()) &&
                        blackFigures.contains(board[to.getRow()][to.getColumn()].getFigure()) ||
                        blackFigures.contains(board[from.getRow()][from.getColumn()].getFigure()) &&
                        whiteFigures.contains(board[to.getRow()][to.getColumn()].getFigure())) {
                    board[to.getRow()][to.getColumn()].setFigure(moveInfo.getFigure());
                } else {
                    throw new RuntimeException("Attempt to attack empty cell or own figure");
                }
                break;
            case PAWN_ATTACK:
                if (Math.abs(from.getColumn() - to.getColumn()) == 1 &&
                        Math.abs(from.getRow() - to.getRow()) == 1) {
                    board[to.getRow()][to.getColumn()].setFigure(moveInfo.getFigure());
                } else {
                    throw new RuntimeException("Illegal pawn attack");
                }
                break;
            case PAWN_LONG_MOVE:
                if (from.getColumn() - to.getColumn() == 0 &&
                        Math.abs(from.getRow() - to.getRow()) == 2 &&
                        board[to.getRow()][to.getColumn()].getFigure() == Figure.NONE) {
                    board[to.getRow()][to.getColumn()].setFigure(moveInfo.getFigure());
                } else {
                    throw new RuntimeException("Illegal long pawn move");
                }
                break;
            case PAWN_ON_GO_ATTACK:
                if (previousChessBoard.moveInfo.getMoveType() == MoveType.PAWN_LONG_MOVE &&
                        previousChessBoard.moveInfo.getCellTo().getColumn() - to.getColumn() == 0) {
                    board[to.getRow()][to.getColumn()].setFigure(moveInfo.getFigure());
                    if (blackFigures.contains(moveInfo.getFigure())) {
                        board[3][to.getColumn()] = new BoardCell(Figure.NONE);
                    } else {
                        board[4][to.getColumn()] = new BoardCell(Figure.NONE);
                    }
                } else {
                    throw new RuntimeException("Illegal pawn on go attack");
                }
                break;
            case PAWN_TO_FIGURE_ATTACK:
            case PAWN_TO_FIGURE:
                throw new RuntimeException("Transformation has not been done yet.");
            case CASTLE_LONG:
                if ((moveInfo.getFigure() == Figure.B_KING ||
                        moveInfo.getFigure() == Figure.W_KING) &&
                        from.getColumn() - to.getColumn() == 2) {
                    board[to.getRow()][to.getColumn()].setFigure(moveInfo.getFigure());
                    // Теперь перемещаем ладью
                    board[to.getRow()][3].setFigure(board[to.getRow()][0].getFigure());
                    board[to.getRow()][0].setFigure(Figure.NONE);
                } else {
                    throw new RuntimeException("Wrong long castling");
                }
                break;
            case CASTLE_SHORT:
                if ((moveInfo.getFigure() == Figure.B_KING ||
                        moveInfo.getFigure() == Figure.W_KING) &&
                        to.getColumn() - from.getColumn() == 2) {
                    board[to.getRow()][to.getColumn()].setFigure(moveInfo.getFigure());
                    // Теперь перемещаем ладью
                    board[to.getRow()][5].setFigure(board[to.getRow()][7].getFigure());
                    board[to.getRow()][7].setFigure(Figure.NONE);
                } else {
                    throw new RuntimeException("Wrong short castling");
                }

        }

        board[from.getRow()][from.getColumn()] = new BoardCell(Figure.NONE);

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
        StringBuilder result = new StringBuilder();
        StringBuilder row = new StringBuilder();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                row.append(figureToSymbol.get(board[i][j].getFigure())).append (" ");
            }
            row.append("\n");
            result.insert(0, row);
            row.delete(0, row.length());
        }

        return result.toString();
    }

}
