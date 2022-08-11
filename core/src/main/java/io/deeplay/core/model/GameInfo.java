package io.deeplay.core.model;

import io.deeplay.core.api.SimpleLogicAppeal;
import io.deeplay.core.api.SimpleLogicCache;
import io.deeplay.core.listener.ChessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameInfo implements ChessListener {
    private GameStatus gameStatus;
    ChessBoard board;
    SimpleLogicAppeal logic;

    // Стандартный конструктор
    public GameInfo() {
        gameStatus = GameStatus.INACTIVE;
        logic = new SimpleLogicCache();
        board = new ChessBoard();
    }

    // Конструктор для заданного расположения фигур.
    public GameInfo(final String fen) {
        this.gameStatus = GameStatus.INACTIVE;
        logic = new SimpleLogicCache();
        board = new ChessBoard(fen);
    }

    @Override
    public void gameStarted() {
        gameStatus = GameStatus.ACTIVE;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public boolean isMate(final ChessBoard chessBoard) {
        return logic.isMate(chessBoard.getFEN());
    }

    public boolean isStalemate(final ChessBoard chessBoard) {
        return logic.isStalemate(chessBoard.getFEN());
    }

    public boolean isDrawByPieceShortage(final ChessBoard chessBoard) {
        return logic.isDrawByPieceShortage(chessBoard.getFEN());
    }

    public boolean isThreefoldRepetition(final ChessBoard chessBoard) {
        return chessBoard.isThreefoldRepetition();
    }

    public boolean isMovesWithoutAttackOrPawnMove(final ChessBoard chessBoard) {
        return chessBoard.getMovesWithoutAttackOrPawnMove() > 99;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public String getFenBoard() {
        return board.getFEN();
    }

    // Обновляет доску, после обновления проверяет достаточно ли материала для мата и было ли трехкратное повторение.
    public void updateBoard(final MoveInfo moveInfo) {
        board.updateBoard(moveInfo);
        checkMatingMaterial();
        if (logic.isMate(board.getFEN())) {
            gameStatus = whoseMove() == Side.WHITE ? GameStatus.BLACK_WON : GameStatus.WHITE_WON;
        }
        if (logic.isStalemate(board.getFEN())) {
            gameStatus = GameStatus.STALEMATE;
        }
        if (board.isThreefoldRepetition()) {
            gameStatus = GameStatus.THREEFOLD_REPETITION;
        }
        // Проверка на правило 50 ходов.
        if (board.getMovesWithoutAttackOrPawnMove() > 99) {
            gameStatus = GameStatus.FIFTY_MOVES_RULE;
        }
    }

    // Проверяет достаточно ли материала для мата. Если нет, меняет статус игры.
    private void checkMatingMaterial() {
        if (logic.isDrawByPieceShortage(board.getFEN())) {
            gameStatus = GameStatus.INSUFFICIENT_MATING_MATERIAL;
        }
    }


    // Возвращает сторону, которая должна сделать ход.
    public Side whoseMove() {
        return board.getWhoseMove();
    }

    public boolean isGameOver() {
        return gameStatus == GameStatus.ACTIVE;
    }

    // Проверка удовлетворяет ли ход правилам шахмат.
    public boolean isMoveValid(final MoveInfo moveInfo) {
        return logic.getMoves(board.getFEN()).contains(moveInfo);
    }

    // Возвращает список всех возможных ходов для конкретной стороны side.
    public Set<MoveInfo> getAvailableMoves() {
        Set<MoveInfo> moves = logic.getMoves(board.getFEN());
        if (moves == null || moves.size() < 1) {
            if (logic.isMate(board.getFEN())) {
                gameStatus = whoseMove() == Side.WHITE ? GameStatus.BLACK_WON : GameStatus.WHITE_WON;
            }
            else {
                gameStatus = GameStatus.STALEMATE;
            }
        }
        return moves;
    }

    /**
     * Возвращает список оставшихся фигур на доске.
     *
     * @return список оставшихся фигур на доске.
     */
    public List<Figure> getAllFigures() {
        List<Figure> allFigures = new ArrayList<>();
        BoardCell[][] boardArray = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (MapsStorage.BLACK_FIGURES.contains(boardArray[i][j].getFigure()) ||
                        MapsStorage.WHITE_FIGURES.contains(boardArray[i][j].getFigure())) {
                    allFigures.add(boardArray[i][j].getFigure());
                }
            }
        }
        return allFigures;
    }

    /**
     * Игнорируется
     *
     * @param side За какую сторону сел игрок
     */
    @Override
    public void playerSeated(final Side side) {}

    /**
     * Совершенный ход
     *
     * @param side     ходившая сторона
     * @param moveInfo совершенный ход
     */
    @Override
    public void playerActed(final Side side, final MoveInfo moveInfo) {
        updateBoard(moveInfo);
    }

    /**
     * @param side Сторона, предложившая ничью
     */
    @Override
    public void offerDraw(final Side side) {
        throw new RuntimeException("Offer draw hasn't been realized yet");
    }

    @Override
    public void acceptDraw(final Side side) {
        throw new RuntimeException("Offer draw hasn't been realized yet");
    }

    /**
     * @param side сторона запросившая отмену хода
     */
    @Override
    public void playerRequestsTakeBack(final Side side) {
        throw new RuntimeException("Take back hasn't been realized yet");
    }

    /**
     * @param side сторона согласившаяся на отмену хода
     */
    @Override
    public void playerAgreesTakeBack(final Side side) {
        throw new RuntimeException("Take back hasn't been realized yet");
    }

    /**
     * @param side сдавшаяся сторона
     */
    @Override
    public void playerResigned(final Side side) {
        throw new RuntimeException("Resign hasn't been realized yet");
    }

    /**
     *
     */
    @Override
    public void draw() {
        gameStatus = GameStatus.DRAW;
    }

    /**
     * @param side победившая сторона
     */
    @Override
    public void playerWon(final Side side) {
        switch (side) {
            case BLACK:
                gameStatus = GameStatus.BLACK_WON;
                break;
            case WHITE:
                gameStatus = GameStatus.WHITE_WON;
                break;
        }
    }
    @Override
    public void gameOver() {}
}
