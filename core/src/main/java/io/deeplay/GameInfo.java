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
    Multimap<Integer, Integer> getAllPossibleMoves(Side side);

    boolean isFinished(int clientId);

    GameStatus getGameStatus(int clientId);

    Side getWinner(int clientId);
    Side getTurnSide(int clientId);
    // TODO: getLastMoveType(int clientId); // рокировка, взятие на проходе и т. д.

    // TODO: getCurrentHistory(int clientId);
    String getCurrentBoardState(int clientId);

    // TODO: getUnsafeCells(int clientId, Side side);
    // TODO: getOccupiedBy(int clientId, Side side);
    // TODO: getSidesAllPiecesOfType(int clientId, Side side, Piece);
    // TODO: getLostPiecesForSide(int clientId, Side side);
}
