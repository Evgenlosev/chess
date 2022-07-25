package io.deeplay.logic.listener;


import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.PieceType;
import io.deeplay.core.settings.GameSettings;

/**
 * Класс с описанием сигнатур методов основных событий в игре
 */
public interface ChessListener {

    void setGameSettings(GameSettings gameSettings);

    /**
     * По настройкам создается доска, бот, бот для подсказки, белой стороне передается ход
     */
    void gameStarted();

    /*
     * Запрос на рекомендацию хода от установленного бота для подсказки
     *
     * Рекомендованный ход
     */
    // MoveInfo recommendedMove(Player player);

    void offerDraw(/*Player player*/);

    void acceptDraw(/*Player player*/);

    void surrender(/*Player player*/);

    void promotion(/*Player player,*/ MoveInfo moveInfo, PieceType pieceType);

    void turn(/*Player player,*/ MoveInfo moveInfo);

    void gameFinished();

}
