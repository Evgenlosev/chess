package io.deeplay.core.listener;


import io.deeplay.core.model.Figure;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;

/**
 * Class should be implemented by each listener.
 */
public interface ChessListener {

//        void setGameSettings(GameSettings gameSettings);

    /**
     *
     */
    void gameStarted();

    void playerSeated(final Side side);

    void playerActed(final Side side, final MoveInfo moveInfo);

    void offerDraw(final Side side);

    void acceptDraw(final Side side);

    void playerRequestsTakeBack(final Side side);

    void playerAgreesTakeBack(final Side side);

    void playerResigned(final Side side);

    void draw();

    void playerWon(final Side side);

    void gameOver();
}
