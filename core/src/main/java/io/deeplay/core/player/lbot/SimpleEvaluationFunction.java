package io.deeplay.core.player.lbot;

import io.deeplay.core.model.Figure;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.Side;

/**
 * Простая функция оценки, которая оценивает состояние игры по сумме полезности оставшихся на поле фигур.
 */
public class SimpleEvaluationFunction implements EvaluationFunction {


    @Override
    public int evaluate(final GameInfo gameinfo) {
        int evaluation = 0;
        if (gameinfo.isMate(gameinfo.getBoard())) {
            return 100;
        }
        for (Figure figure : gameinfo.getAllFigures()) {
            evaluation += figure.getWeight();
        }
        if (gameinfo.whoseMove() == Side.WHITE) {
            return evaluation * (-1);
        }
        return evaluation;
    }

}
