package io.deeplay.core.model;

import io.deeplay.core.api.SimpleLogic;
import io.deeplay.core.api.SimpleLogicAppeal;
import io.deeplay.core.listener.ChessListener;

import java.util.List;
import java.util.Set;

public class GameInfo implements ChessListener {
    private GameStatus gameStatus;
    ChessBoard board;
    private boolean isDraw = false;
    private boolean isGameOver = false;
    private Side winner = null;

    SimpleLogicAppeal logic = new SimpleLogic();

    public GameInfo() {
        board = new ChessBoard();
        this.gameStatus = GameStatus.INACTIVE;
    }

    public GameInfo(final ChessBoard board) {
        this.board = board;
        this.gameStatus = GameStatus.INACTIVE;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
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
        if (gameStatus == GameStatus.ACTIVE) return true;
        return false;
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
     *
     */
    @Override
    public void gameStarted() {
        this.gameStatus = GameStatus.ACTIVE;
    }

    /**
     * Игнорируется
     * @param side За какую сторону сел игрок
     */
    @Override
    public void playerSeated(final Side side) {

    }

    /**
     * Совершенный ход
     * @param side ходившая сторона
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

    }

    /**
     * @param side
     */
    @Override
    public void acceptDraw(final Side side) {
        this.isDraw = true;
    }

    /**
     * @param side сторона запросившая отмену хода
     */
    @Override
    public void playerRequestsTakeBack(final Side side) {

    }

    /**
     * @param side сторона согласившаяся на отмену хода
     */
    @Override
    public void playerAgreesTakeBack(final Side side) {

    }

    /**
     * @param side сдавшаяся сторона
     */
    @Override
    public void playerResigned(final Side side) {

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
