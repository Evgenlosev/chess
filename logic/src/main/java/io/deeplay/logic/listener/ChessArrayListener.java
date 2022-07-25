package io.deeplay.logic.listener;

import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.PieceType;
import io.deeplay.core.player.Player;
import io.deeplay.core.settings.GameSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс хранит в себе всех наблюдателей, что упрощает процесс оповещения
 */
public class ChessArrayListener implements ChessListener {
    private final List<ChessListener> listeners = new ArrayList<>();

    public ChessListener getListenerAtIndex(int index) {
        return listeners.get(index);
    }

    public void addListener(ChessListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void setGameSettings(final GameSettings gameSettings) {
        listeners.forEach(listener -> listener.setGameSettings(gameSettings));
    }

    @Override
    public void gameStarted() {
        listeners.forEach(ChessListener::gameStarted);
    }

    @Override
    public void recommendedMove(final Player player) {
        listeners.forEach(listener -> listener.recommendedMove(player));
    }

    @Override
    public void offerDraw(final Player player) {
        listeners.forEach(listener -> listener.offerDraw(player));
    }

    @Override
    public void acceptDraw(final Player player) {
        listeners.forEach(listener -> listener.acceptDraw(player));
    }

    @Override
    public void resign(final Player player) {
        listeners.forEach(listener -> listener.resign(player));
    }

    @Override
    public void pawnPromotion(final Player player, final MoveInfo moveInfo, final PieceType pieceType) {
        listeners.forEach(listener -> listener.pawnPromotion(player, moveInfo, pieceType));
    }

    @Override
    public void turn(final Player player, final MoveInfo moveInfo) {
        listeners.forEach(listener -> listener.turn(player, moveInfo));
    }

    @Override
    public void gameFinished() {
        listeners.forEach(ChessListener::gameFinished);
    }
}
