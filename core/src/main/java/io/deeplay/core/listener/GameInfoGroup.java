package io.deeplay.core.listener;

import io.deeplay.core.model.GameInfo;

/**
 * Class stores all listeners and notify them, as a chessEventSource, except GameInfo is notified first.
 */
public class GameInfoGroup extends ChessEventSource {
    public GameInfoGroup(GameInfo gameInfo) {
        super();
        listeners.add(gameInfo);
    }
}
