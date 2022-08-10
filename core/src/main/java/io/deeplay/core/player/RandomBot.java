package io.deeplay.core.player;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import org.slf4j.LoggerFactory;


import java.util.Random;
import java.util.Set;

public class RandomBot extends Player {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(RandomBot.class);
    private final Random random;

    public RandomBot(final Side side) {
        this(side, System.currentTimeMillis());
    }

    public RandomBot(final Side side, final long seed) {
        this.side = side;
        this.random = new Random(seed);
        LOGGER.info("Для {} установлен seed - {}", this, seed);
    }

    /**
     * Возвращает рандомный ход
     * @param gameInfo - текущее состоние партии
     */
    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        final Set<MoveInfo> allMoves = gameInfo.getAvailableMoves();
        final int randomMoveNumber = random.nextInt(allMoves.size());
        return (MoveInfo) allMoves.toArray()[randomMoveNumber];
    }
}
