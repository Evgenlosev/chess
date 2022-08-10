package io.deeplay.core.chadBot;

import io.deeplay.core.api.SimpleLogic;
import io.deeplay.core.api.SimpleLogicAppeal;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;

public class ChadBot extends Player {
    EvalFunction evalFunction;
    SimpleLogicAppeal logic;

    public ChadBot(Side side) {
        this.side = side;
        evalFunction = new SimpleEvalFunction();
        logic = new SimpleLogic();
    }

    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        double bestMoveValue = Integer.MIN_VALUE;
        MoveInfo bestMove = null;
        for (MoveInfo move : gameInfo.getAvailableMoves()) {
            gameInfo.updateBoardWithoutUpdatingStatus(move);
            double value = evalFunction.eval(gameInfo);
            if (logic.isMate(gameInfo.getFenBoard())) {
                gameInfo.undo();
                return move;
            }
            gameInfo.undo();
            if (value >= bestMoveValue) {
                bestMoveValue = value;
                bestMove = move;
            }
        }
        return bestMove;
    }
}
