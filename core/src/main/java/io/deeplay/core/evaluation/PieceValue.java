package io.deeplay.core.evaluation;

import io.deeplay.core.model.ChessBoard;
import io.deeplay.core.model.Side;

public class PieceValue implements Evaluation {

    @Override
    public int evaluateBoard(final ChessBoard chessBoard) {
        return chessBoard.countPiecesValuesForSide(Side.WHITE) - chessBoard.countPiecesValuesForSide(Side.BLACK);
    }

}