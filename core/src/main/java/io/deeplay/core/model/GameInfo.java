package io.deeplay.core.model;

import io.deeplay.core.api.SimpleLogicAppeal;
import io.deeplay.core.listener.ChessListener;

import java.util.List;
import java.util.Set;

public class GameInfo implements ChessListener {

    ChessBoard board;
    private boolean isDraw = false;
    private boolean isGameOver = false;
    private Side winner = null;

    SimpleLogicAppeal logic = new SimpleLogicAppeal() {
        @Override
        public boolean isMate(String fenNotation) {
            return false;
        }

        @Override
        public boolean isStalemate(String fenNotation) {
            return false;
        }

        @Override
        public Set<MoveInfo> getMoves(String fenNotation) {
            return null;
        }
    };

    public GameInfo() {
        board = new ChessBoard();
    }

    public GameInfo(final ChessBoard board) {
        this.board = board;
    }

    public String getFenBoard() {
        return board.getFEN();
    }

    public void updateBoard(final MoveInfo moveInfo) {
        board.updateBoard(moveInfo);
    }

    public Side whoseMove() {
        return board.getWhoseMove();
    }

    public boolean isGameOver() {
        return logic.isMate(board.getFEN()) || logic.isStalemate(board.getFEN()) || isDraw;
    }

    public boolean isMoveValid(final MoveInfo moveInfo) {
        return logic.getMoves(board.getFEN()).contains(moveInfo);
    }

    public Set<MoveInfo> getAvailableMoves(final Side side) {
        return logic.getMoves(board.getFEN());
    }

    /**
     * Возвращает список оставшихся фигур на доске
     * @param side цвет игрока
     * @return
     */
    public List<Figure> getSideFigures(final Side side) {
        return null;
    }


    /**
     * Получает событие начала игры, игнорирует его
     */
    @Override
    public void gameStarted() {}

    /**
     * Игнорируется
     * @param side За какую сторону сел игрок
     */
    @Override
    public void playerSeated(Side side) {}

    /**
     * Совершенный ход
     * @param side ходившая сторона
     * @param moveInfo совершенный ход
     */
    @Override
    public void playerActed(Side side, MoveInfo moveInfo) {
        board.updateBoard(moveInfo);
    }

    /**
     * @param side Сторона, предложившая ничью
     */
    @Override
    public void offerDraw(Side side) {}

    /**
     * @param side сторона принявшая ничью
     */
    @Override
    public void acceptDraw(Side side) {
        this.isDraw = true;
    }

    /**
     * @param side сторона запросившая отмену хода
     */
    @Override
    public void playerRequestsTakeBack(Side side) {}

    /**
     * @param side сторона согласившаяся на отмену хода
     */
    @Override
    public void playerAgreesTakeBack(Side side) {}

    /**
     * @param side сдавшаяся сторона
     */
    @Override
    public void playerResigned(Side side) {

    }

    /**
     *
     */
    @Override
    public void draw() {

    }

    /**
     * @param side победившая сторона
     */
    @Override
    public void playerWon(final Side side) {
        winner = side;
    }

    /**
     *
     */
    @Override
    public void gameOver() {
        isGameOver = true;
    }
}
