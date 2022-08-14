package io.deeplay.core.lbot;

import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;

import java.util.Set;

public class LBot extends Player {

    private final EvaluationFunction function;

    public LBot(final Side side, final EvaluationFunction function) {
        super(side);
        this.function = function;
    }

    public LBot(final Side side) {
        this(side, new SimpleEvaluationFunction());
    }

    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        int bestEvaluation = Integer.MIN_VALUE;
        MoveInfo bestMove = null;
        GameInfo clonedGameInfo;
        final Set<MoveInfo> availableMoves = gameInfo.getAvailableMoves();
        for (MoveInfo moveInfo : availableMoves) {
            clonedGameInfo = new GameInfo(gameInfo);
            clonedGameInfo.updateBoard(moveInfo);
            if (clonedGameInfo.isMate(clonedGameInfo.getBoard())) {
                return moveInfo;
            }
            int moveEvaluation = function.evaluate(clonedGameInfo);
            if (moveEvaluation > bestEvaluation) {
                bestEvaluation = moveEvaluation;
                bestMove = moveInfo;
            }
        }
        return bestMove;
    }
}
