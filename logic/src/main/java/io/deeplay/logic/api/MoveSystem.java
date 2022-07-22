package io.deeplay.logic.api;

import com.google.common.collect.Multimap;
import io.deeplay.core.model.ChessBoard;
import io.deeplay.core.model.Coord;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;

import java.util.Set;

public interface MoveSystem {

    /**
     * @param board текущее состояние шахматной доски
     * @param from изначальная позиция пешки
     * @return возвращает все возможные ходы пешки
     */
    Set<MoveInfo> getPawnMoves(ChessBoard board, Coord from);

    /**
     * @param board текущее состояние шахматной доски
     * @param from изначальная позиция коня
     * @return возвращает все возможные ходы коня
     */
    Set<MoveInfo> getKnightMoves(ChessBoard board, Coord from);

    /**
     * @param board текущее состояние шахматной доски
     * @param from изначальная позиция слона
     * @return возвращает все возможные ходы слона
     */
    Set<MoveInfo> getBishopMoves(ChessBoard board, Coord from);

    /**
     * @param board текущее состояние шахматной доски
     * @param from изначальная позиция ладьи
     * @return возвращает все возможные ходы ладьи
     */
    Set<MoveInfo> getRookMoves(ChessBoard board, Coord from);

    /**
     * @param board текущее состояние шахматной доски
     * @param from изначальная позиция ферзя
     * @return возвращает все возможные ходы ферзя
     */
    Set<MoveInfo> getQueenMoves(ChessBoard board, Coord from);

    /**
     * @param board текущее состояние шахматной доски
     * @param from изначальная позиция короля
     * @return возвращает все возможные ходы короля
     */
    Set<MoveInfo> getKingMoves(ChessBoard board, Coord from);

    /**
     * @param board текущее состояние шахматной доски
     * @param side
     * @return key - изначальная позиция фигуры, value - все возможные ходы
     */
    Multimap<Coord, MoveInfo> getAllPossibleMoves(ChessBoard board, Side side);

    /**
     * @param board текущее состояние шахматной доски
     * @param side
     * @return true если королю side поставлен шах
     */
    boolean isCheck(ChessBoard board, Side side);

}
