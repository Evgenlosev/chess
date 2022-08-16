package io.deeplay.server.session;

import io.deeplay.core.SelfPlay;
import io.deeplay.core.player.Player;

import java.util.UUID;

public class GameSession implements Runnable {
    private final String sessionToken;
    private final Player firstPlayer;
    private final Player secondPlayer;

    public GameSession(final Player firstPlayer, final Player secondPlayer) {
        this.sessionToken = UUID.randomUUID().toString();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    @Override
    public void run() {
        try {
            new SelfPlay(firstPlayer, secondPlayer).play();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
