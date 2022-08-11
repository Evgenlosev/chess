package io.deeplay.core.player.vladbot;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.evaluation.Evaluation;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;
import org.slf4j.LoggerFactory;

public abstract class VBot extends Player {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(VBot.class);

    private final Evaluation evaluation;

    public VBot(Side side, Evaluation evaluation) {
        super(side);
        this.evaluation = evaluation;
        LOGGER.info("Для {} установлена оценочная функция - {}", this, evaluation);
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    @Override
    public abstract MoveInfo getAnswer(GameInfo gameInfo);

}
