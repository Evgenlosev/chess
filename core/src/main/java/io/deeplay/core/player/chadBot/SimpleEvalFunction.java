package io.deeplay.core.player.chadBot;

import io.deeplay.core.model.Figure;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.GameStatus;

public class SimpleEvalFunction extends EvalFunction{
    @Override
    public Double eval(GameInfo gameinfo) {
        double evaluationValue = 0;
        if (gameinfo.getGameStatus() == GameStatus.BLACK_WON) {
            return -100000.0;
        } else if (gameinfo.getGameStatus() == GameStatus.WHITE_WON) {
            return 100000.0;
        } else if (gameinfo.getGameStatus() == GameStatus.DRAW ||
        gameinfo.getGameStatus() == GameStatus.FIFTY_MOVES_RULE ||
        gameinfo.getGameStatus() == GameStatus.STALEMATE ||
        gameinfo.getGameStatus() == GameStatus.THREEFOLD_REPETITION) {
            return 0.0;
        }
        for (Figure figure : gameinfo.getAllFigures()) {
            evaluationValue += figure.getWeight();
        }
        return evaluationValue;
    }
}
