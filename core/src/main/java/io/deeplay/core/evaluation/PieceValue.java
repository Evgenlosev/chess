package io.deeplay.core.evaluation;

import io.deeplay.core.model.ChessBoard;
import io.deeplay.core.model.Side;

/**
 * Функция оценки основанная только на ценности фигур.
 * Данная оценка плоха тем, что не развивает фигуры, поэтому теряет большинство потенциала.
 */
public class PieceValue implements Evaluation {

    @Override
    public int evaluateBoard(final ChessBoard chessBoard) {
        return chessBoard.countPiecesValuesForSide(Side.WHITE) - chessBoard.countPiecesValuesForSide(Side.BLACK);
    }

}