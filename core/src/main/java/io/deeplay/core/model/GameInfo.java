package io.deeplay.core.model;

import com.google.common.collect.Multimap;
import io.deeplay.core.listener.ChessListener;

import java.util.List;

public class GameInfo implements ChessListener {

    ChessBoard board;

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

    }

    /**
     * @param side
     */
    @Override
    public void playerSeated(Side side) {

    }

    /**
     * @param side
     * @param moveInfo
     */
    @Override
    public void playerActed(Side side, MoveInfo moveInfo) {

    }

    /**
     * @param side
     */
    @Override
    public void offerDraw(Side side) {

    }

    /**
     * @param side
     */
    @Override
    public void acceptDraw(Side side) {

    }

    /**
     * @param side
     */
    @Override
    public void playerRequestsTakeBack(Side side) {

    }

    /**
     * @param side
     */
    @Override
    public void playerAgreesTakeBack(Side side) {

    }

    /**
     * @param side
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
     * @param side
     */
    @Override
    public void playerWon(Side side) {

    }

    /**
     *
     */
    @Override
    public void gameOver() {

    }
}
