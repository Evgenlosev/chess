package io.deeplay.core.observer;

import com.google.common.collect.Multimap;
import io.deeplay.core.model.GameStatus;
import io.deeplay.core.model.PieceType;
import io.deeplay.core.model.Side;

/**
 * Класс хранит и отдаёт информацию об игре
 */
public class GameInfo implements ChessObserver {
    // Доска
    // Оба игрока
    // Никакие id не нужны, т.к. GI привязан к игре


    /**
     * @param side - сторона у которой мы получаем все возможные ходы
     * @return key - изначальная позиция фигуры, value - конечные позиции
     */
    Multimap<Integer, Integer> getAllPossibleMoves(int clientId, Side side) {

    }

    boolean isFinished(int clientId) {

    }

    GameStatus getGameStatus(int clientId) {

    }

    Side getWinner(int clientId) {

    }

    Side getTurnSide(int clientId) {

    }

    // TODO: getLastMoveType(int clientId); // рокировка, взятие на проходе и т. д.

    // TODO: getCurrentHistory(int clientId);

    /**
     * @param clientId
     * @return возвращает строку в нотации FEN
     */
    String getCurrentBoardState(int clientId) {

    }

    /**
     * @param clientId
     * @param side
     * @return все фигуры на доске, для выбранной стороны
     */
    Multimap<PieceType, Integer> getSideAlivePieces(int clientId, Side side) {

    }

    /**
     * @param clientId
     * @param side
     * @return все фигуры на доске, для выбранной стороны
     */
    Multimap<PieceType, Integer> getSideLostPieces(int clientId, Side side) {

    }
}
