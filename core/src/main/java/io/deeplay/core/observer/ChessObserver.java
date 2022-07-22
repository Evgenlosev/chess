package io.deeplay.core.observer;


import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.settings.GameSettings;

/**
 * Класс с описанием сигнатур методов основных событий в игре
 */
public interface ChessObserver {
    // TODO: стандартная реализация observer в java

    void setGameSettings(GameSettings gameSettings);

    /**
     * По настройкам создается доска, бот, бот для подсказки, белой стороне передается ход
     */
    void gameStarted(); // не ChessObserver же будет создавать

    /**
     * Запрос на рекомендацию хода от установленного бота для подсказки
     *
     * @return Рекомендованный ход
     */
    MoveInfo recommendedMove(/*player*/);

    void turn(/*player, MoveInfo*/); // dto?

    void gameFinished();

}
