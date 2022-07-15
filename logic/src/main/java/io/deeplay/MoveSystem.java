package io.deeplay;

import com.google.common.collect.Multimap;
import io.deeplay.model.ChessBoard;
import io.deeplay.model.Side;

import java.util.List;
import java.util.Set;

public interface MoveSystem {

    /**
     * @param board
     * @param fromCellIndex изначальная позиция пешки
     * @param side
     * @return возвращает все возможные конечные клетки для хода пешки
     */
    Set<Integer> getPawnMoves(ChessBoard board, int fromCellIndex, Side side);

    /**
     * @param board
     * @param fromCellIndex изначальная позиция коня
     * @param side
     * @return возвращает все возможные конечные клетки для хода коня
     */
    Set<Integer> getKnightMoves(ChessBoard board, int fromCellIndex, Side side);

    /**
     * @param board
     * @param fromCellIndex изначальная позиция слона
     * @param side
     * @return возвращает все возможные конечные клетки для хода слона
     */
    Set<Integer> getBishopMoves(ChessBoard board, int fromCellIndex, Side side);

    /**
     * @param board
     * @param fromCellIndex изначальная позиция ладьи
     * @param side
     * @return возвращает все возможные конечные клетки для хода ладьи
     */
    Set<Integer> getRookMoves(ChessBoard board, int fromCellIndex, Side side);

    /**
     * @param board
     * @param fromCellIndex изначальная позиция королевы
     * @param side
     * @return возвращает все возможные конечные клетки для хода королевы
     */
    Set<Integer> getQueenMoves(ChessBoard board, int fromCellIndex, Side side);

    /**
     * @param board
     * @param side
     * @return возвращает все возможные конечные клетки для хода короля
     */
    Set<Integer> getKingMoves(ChessBoard board, Side side);

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
    boolean isKingUnderAttack(ChessBoard board, Side side);

    /**
     * @param board
     * @param side
     * @return true если король side не может походить
     */
    boolean isKingCantMove(ChessBoard board, Side side); // getKingMoves(...) == 0

    /**
     * @param board
     * @param side
     * @return true если есть фигуры которые могут спасти короля side от мата
     */
    boolean hasKingCoverMoves(ChessBoard board, Side side);

}
