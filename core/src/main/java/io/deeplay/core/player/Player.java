package io.deeplay.core.player;

import io.deeplay.core.listener.ChessListener;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;

public abstract class Player implements ChessListener {
    private Side side;

    public Player(final Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(final Side side) {
        this.side = side;
    }

    public abstract MoveInfo getAnswer(GameInfo gameInfo);

    @Override
    public String toString() {
        return side.toString();

    }

    @Override
    public void gameStarted() {

    }

    @Override
    public void playerSeated(Side side) {

    }

    @Override
    public void playerActed(Side side, MoveInfo moveInfo) {

    }

    @Override
    public void offerDraw(Side side) {

    }

    @Override
    public void acceptDraw(Side side) {

    }

    @Override
    public void playerRequestsTakeBack(Side side) {

    }

    @Override
    public void playerAgreesTakeBack(Side side) {

    }

    @Override
    public void playerResigned(Side side) {

    }

    @Override
    public void draw() {

    }

    @Override
    public void playerWon(Side side) {

    }

    @Override
    public void gameOver() {

    }
}
