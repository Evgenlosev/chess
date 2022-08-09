package io.deeplay.core.listener;

import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;

/**
 * Интерфейс должен быть имплементирован каждым слушателем.
 */
public interface ChessListener {
    // Игра началась
    void gameStarted();
    // Игрок занял сторону
    void playerSeated(final Side side);
    // Игрок сделал ход
    void playerActed(final Side side, final MoveInfo moveInfo);
    // Игрок предложил ничью
    void offerDraw(final Side side);
    // Игрок согласился на ничью
    void acceptDraw(final Side side);
    // Запрос отмены хода
    void playerRequestsTakeBack(final Side side);
    // Отмена хода принята
    void playerAgreesTakeBack(final Side side);
    // Игрок сдался
    void playerResigned(final Side side);
    // Произошла ничья
    void draw();
    // Игрок победил
    void playerWon(final Side side);
    // Игра окончена
    void gameOver();
}
