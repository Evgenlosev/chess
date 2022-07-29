package io.deeplay.core.model;

import com.google.common.collect.Multimap;
import io.deeplay.core.listener.ChessListener;

import java.util.List;

public class GameInfo implements ChessListener {
    private GameStatus gameStatus;
    ChessBoard board;

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
        if (gameStatus == GameStatus.ACTIVE || gameStatus == GameStatus.INACTIVE) return true;
        return false;
    }

    public boolean isMoveValid(final MoveInfo moveInfo) {
        return true;
    }

    public Multimap<Coord, MoveInfo> getAvailableMoves(final Side side) {
        return null;
    }

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
     * @param side
     */
    @Override
    public void playerSeated(final Side side) {

    }

    /**
     * @param side
     * @param moveInfo
     */
    @Override
    public void playerActed(final Side side, final MoveInfo moveInfo) {
        updateBoard(moveInfo);
    }

    /**
     * @param side
     */
    @Override
    public void offerDraw(final Side side) {

    }

    /**
     * @param side
     */
    @Override
    public void acceptDraw(final Side side) {

    }

    /**
     * @param side
     */
    @Override
    public void playerRequestsTakeBack(final Side side) {

    }

    /**
     * @param side
     */
    @Override
    public void playerAgreesTakeBack(final Side side) {

    }

    /**
     * @param side
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
     * @param side
     */
    @Override
    public void playerWon(final Side side) {

    }

    /**
     *
     */
    @Override
    public void gameOver() {

    }
}
