package io.deeplay;

import com.google.common.collect.Multimap;
import io.deeplay.logic.board.ChessBoard;
import io.deeplay.core.model.Side;

import java.util.Set;

public interface MoveSystem {

    /**
     * @param board
     * @param fromCellIndex изначальная позиция пешки
     * @return возвращает все возможные конечные клетки для хода пешки
     */
    Set<Integer> getPawnMoves(ChessBoard board, int fromCellIndex);

    /**
     * @param board
     * @param fromCellIndex изначальная позиция коня
     * @return возвращает все возможные конечные клетки для хода коня
     */
    Set<Integer> getKnightMoves(ChessBoard board, int fromCellIndex);

    /**
     * @param board
     * @param fromCellIndex изначальная позиция слона
     * @return возвращает все возможные конечные клетки для хода слона
     */
    Set<Integer> getBishopMoves(ChessBoard board, int fromCellIndex);

    /**
     * @param board
     * @param fromCellIndex изначальная позиция ладьи
     * @return возвращает все возможные конечные клетки для хода ладьи
     */
    Set<Integer> getRookMoves(ChessBoard board, int fromCellIndex);

    /**
     * @param board
     * @param fromCellIndex изначальная позиция королевы
     * @return возвращает все возможные конечные клетки для хода королевы
     */
    Set<Integer> getQueenMoves(ChessBoard board, int fromCellIndex);

    /**
     * @param board
     * @param fromCellIndex изначальная позиция короля
     * @return возвращает все возможные конечные клетки для хода короля
     */
    Set<Integer> getKingMoves(ChessBoard board, int fromCellIndex);

    /**
     * @param board
     * @param side
     * @return key - изначальная позиция фигуры, value - конечные позиции
     */
    Multimap<Integer, Integer> getAllPossibleMoves(ChessBoard board, Side side);

    /**
     * @param board
     * @param side
     * @return true если королю side поставлен шах
     */
    boolean isCheck(ChessBoard board, Side side);

}
