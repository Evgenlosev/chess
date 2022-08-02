package io.deeplay.core.player;

import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;

import java.util.Random;
import java.util.Set;

public class RandomBot extends Player {

    private final Random random;

    public RandomBot(final Side side) {
        super(side);
        this.random = new Random(System.currentTimeMillis());
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
