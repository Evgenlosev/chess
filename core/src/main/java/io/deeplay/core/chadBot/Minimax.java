package io.deeplay.core.chadBot;

import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;

public class Minimax extends Player {
    EvalFunction evalFunction;
    final int depth;

    public Minimax(Side side) {
        this(side,3);
    }

    public Minimax(final Side side, final int depth) {
        this.depth = depth;
        this.side = side;
        evalFunction = new SimpleEvalFunction();
    }

    public MoveInfo minimaxRoot(final GameInfo gameInfo, final int depth, final boolean isMaximising) {
        double bestMove = Integer.MIN_VALUE;
        MoveInfo bestMoveInfo = null;
        for (MoveInfo move : gameInfo.getAvailableMoves()) {
            gameInfo.updateBoardWithoutUpdatingStatus(move);
            double value = minimax(gameInfo, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, !isMaximising);
            gameInfo.undo();
            if (value >= bestMove) {
                bestMove = value;
                bestMoveInfo = move;
            }
        }
        return bestMoveInfo;
    }
    private Double minimax(final GameInfo gameInfo, final int depth, double alpha, double beta, final boolean isMaximising) {
        if (depth == 0) {
            return side == Side.WHITE? evalFunction.eval(gameInfo) : -evalFunction.eval(gameInfo);
        }

        double bestMoveValue;
        if (isMaximising) {
            bestMoveValue = Integer.MIN_VALUE;
            for (MoveInfo move : gameInfo.getAvailableMoves()) {
                gameInfo.updateBoardWithoutUpdatingStatus(move);
                bestMoveValue = Math.max(bestMoveValue, minimax(gameInfo, depth - 1, alpha, beta, false));
                gameInfo.undo();
                alpha = Math.max(alpha, bestMoveValue);
                if (beta <= alpha) {
                    return bestMoveValue;
                }
            }
        } else {
            bestMoveValue = Integer.MAX_VALUE;
            for (MoveInfo move : gameInfo.getAvailableMoves()) {
                gameInfo.updateBoardWithoutUpdatingStatus(move);
                bestMoveValue = Math.min(bestMoveValue, minimax(gameInfo, depth - 1, alpha, beta, true));
                gameInfo.undo();
                beta = Math.min(beta, bestMoveValue);
                if (beta <= alpha) {
                    return bestMoveValue;
                }
            }
        }
        return bestMoveValue;
    }

    @Override
    public MoveInfo getAnswer(GameInfo gameInfo) {
        return minimaxRoot(gameInfo, depth, true);
    }
}
