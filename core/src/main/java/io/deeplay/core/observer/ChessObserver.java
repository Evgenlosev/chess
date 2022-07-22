package io.deeplay.core.observer;


import io.deeplay.core.settings.GameSettings;

/**
 * Класс с описанием сигнатур методов основных событий в игре
 */
public interface ChessObserver {
    // TODO: стандартная реализация observer в java

    void gameSettingSet(GameSettings gameSettings);

    /**
     * По настройкам создается доска, бот, бот для подсказки, белой стороне передается ход
     */
    void gameStarted();

    List<MoveInfo>

    void turn(/*player, MoveInfo*/); // dto?

    void gameFinished();

}
