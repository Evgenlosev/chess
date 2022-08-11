package io.deeplay.core.listener;

import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;

public class ChessAdapter implements ChessListener{
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
