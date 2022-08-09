package io.deeplay.core.chadBot;

import io.deeplay.core.api.SimpleLogic;
import io.deeplay.core.api.SimpleLogicAppeal;
import io.deeplay.core.model.Figure;
import io.deeplay.core.model.GameInfo;

public class SimpleEvalFunction extends EvalFunction{
    SimpleLogicAppeal logic = new SimpleLogic();
    @Override
    public Double eval(GameInfo gameinfo) {
        double evaluationValue = 0;
        if (logic.isMate(gameinfo.getFenBoard())) {
            return 100000.0;
        }
        for (Figure figure : gameinfo.getAllFigures()) {
            evaluationValue += figure.getWeight();
        }
        return evaluationValue;
    }
}
