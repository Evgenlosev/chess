package io.deeplay.core.player;

import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;

import java.util.Random;
import java.util.Set;

public class RandomBot extends Player {
    private final PlayerType playerType;

    public RandomBot(final Side side, final int id) {
        super(side, id);
        this.playerType = PlayerType.RANDOM_BOT;
    }

    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        //TODO: должен быть реализован метод, который возвращает set
        Set<MoveInfo> allMoves = gameInfo.getAvailableMoves(this.getSide());
        // TODO: исправить IllegalArgumentException: bound must be positive, не работает с нулем, лучше просто проверку на ноль, а после проверить на мат и пат
        int randomMoveNumber = new Random().nextInt(allMoves.size());
        int i = 0;
        for (MoveInfo moveInfo : allMoves) {
            if (i == randomMoveNumber) {
                return moveInfo;
            }
            i++;
        }
        return null;
    }
}
