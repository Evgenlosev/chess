package io.deeplay.core.player;

import io.deeplay.core.model.Side;

public class PlayerFactory {
    public static Player createPlayer(final PlayerType playerType, final Side side) {
        Player player;
        switch (playerType) {
            case HUMAN:
                player = new HumanPlayer(side);
                break;
            case RANDOM_BOT:
                player = new RandomBot(side);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported player type - " + playerType);
        }
        return player;
    }
}

