package io.deeplay.core.player.vladbot;

import io.deeplay.core.evaluation.vladevaluations.Evaluation;
import io.deeplay.core.evaluation.vladevaluations.PeSTO;
import io.deeplay.core.model.*;
import io.deeplay.core.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class VBot extends Player {
    private static final Logger LOGGER = LoggerFactory.getLogger(VBot.class);

    private final Evaluation evaluation;

    public VBot(final Side side) {
        this(side, new PeSTO());
    }

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
