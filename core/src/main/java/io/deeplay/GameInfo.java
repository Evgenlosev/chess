package io.deeplay;

import com.google.common.collect.Multimap;
import io.deeplay.model.GameStatus;
import io.deeplay.model.PieceType;
import io.deeplay.model.Side;

public interface GameInfo {

    /**
     * @param side - сторона у которой мы получаем все возможные ходы
     * @return key - изначальная позиция фигуры, value - конечные позиции
     */
    Multimap<Integer, Integer> getAllPossibleMoves(int clientId, Side side);

    boolean isFinished(int clientId);

    GameStatus getGameStatus(int clientId);

    Side getWinner(int clientId);

    Side getTurnSide(int clientId);

    // TODO: getLastMoveType(int clientId); // рокировка, взятие на проходе и т. д.

    // TODO: getCurrentHistory(int clientId);

    /**
     * @param clientId
     * @return возвращает строку в нотации FEN
     */
    String getCurrentBoardState(int clientId);

    /**
     * @param clientId
     * @param side
     * @return все фигуры на доске, для выбранной стороны
     */
    Multimap<PieceType, Integer> getSideAlivePieces(int clientId, Side side);

    /**
     * @param clientId
     * @param side
     * @return все фигуры на доске, для выбранной стороны
     */
    Multimap<PieceType, Integer> getSideLostPieces(int clientId, Side side);
}
