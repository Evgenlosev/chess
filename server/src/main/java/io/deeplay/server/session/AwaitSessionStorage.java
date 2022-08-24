package io.deeplay.server.session;

import java.util.concurrent.CopyOnWriteArrayList;

public class AwaitSessionStorage {
    private static final CopyOnWriteArrayList<GameSession> AWAIT_SESSIONS = new CopyOnWriteArrayList<>();

    public static boolean add(final GameSession gameSession) {
        return AWAIT_SESSIONS.addIfAbsent(gameSession);
    }

    public static boolean remove(final GameSession gameSession) {
        return AWAIT_SESSIONS.remove(gameSession);
    }

    public static boolean isEmpty() {
        return AWAIT_SESSIONS.isEmpty();
    }

    public static GameSession getAwaitSession() {
        return AWAIT_SESSIONS.get(0);
    }
}
