package io.deeplay.core.player;

import io.deeplay.core.model.Side;
import io.deeplay.core.player.evgenbot.EvgenBot;

public class PlayerFactory {
    public static Player createPlayer(final PlayerType playerType, final Side side) {
        switch (playerType) {
            case HUMAN:
                return new HumanPlayer(side);
            case RANDOM_BOT:
                return new RandomBot(side);
            case EVGEN_BOT:
                return new EvgenBot(side);
            default:
                throw new UnsupportedOperationException("Unsupported player type - " + playerType);
        }
    }
}

