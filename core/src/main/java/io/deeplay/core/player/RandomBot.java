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
        super(side);
        long seed = System.currentTimeMillis();
        this.random = new Random(seed);
        LOGGER.info("Для {} установлен seed - {}", this, seed);
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.RANDOM_BOT;
    }

    /**
     * Возвращает рандомный ход
     * @param gameInfo - текущее состоние партии
     */
    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        Set<MoveInfo> allMoves = gameInfo.getAvailableMoves();
        int randomMoveNumber = random.nextInt(allMoves.size());
        return (MoveInfo) allMoves.toArray()[randomMoveNumber];
    }
}
