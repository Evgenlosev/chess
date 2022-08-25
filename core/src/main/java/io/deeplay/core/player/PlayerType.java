package io.deeplay.core.player;

public enum PlayerType {
    HUMAN("Человек"),
    EVGEN_BOT("Бот Евгения"),
    RANDOM_BOT("Рандомный бот");

    private final String description;

    PlayerType(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
