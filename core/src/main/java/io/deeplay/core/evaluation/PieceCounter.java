package io.deeplay.core.evaluation;

import io.deeplay.core.model.ChessBoard;
import io.deeplay.core.model.Side;

/**
 * Функция оценки основанная только на количестве фигур.
 * Данная оценка плоха тем, что не развивает фигуры, поэтому теряет большинство потенциала.
 */
public class PieceCounter implements Evaluation {

    @Override
    public int evaluateBoard(final ChessBoard chessBoard) {
        return chessBoard.countPiecesForSide(Side.WHITE) - chessBoard.countPiecesForSide(Side.BLACK);
    }

}