package io.deeplay.server.session;

import io.deeplay.core.SelfPlay;
import io.deeplay.core.model.GameStatus;
import io.deeplay.core.player.Player;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameSession {
    private final String sessionToken;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private final SelfPlay selfPlay;

    private final ExecutorService executorService;

    public String getSessionToken() {
        return sessionToken;
    }

    public GameSession(final Player firstPlayer, final Player secondPlayer) {
        this.sessionToken = UUID.randomUUID().toString();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.selfPlay = new SelfPlay(firstPlayer, secondPlayer);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void start() {
        executorService.execute(() -> {
            try {
                selfPlay.play();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                executorService.shutdown();
            }
        });
    }

    public void stopSession(final GameStatus gameStatus) {
        selfPlay.getGameInfo().setGameStatus(gameStatus);
        executorService.shutdownNow();
    }
}
