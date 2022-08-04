package io.deeplay.server.client;

import io.deeplay.core.model.Side;
import io.deeplay.core.player.HumanPlayer;
import io.deeplay.core.player.Player;
import io.deeplay.core.player.PlayerType;
import io.deeplay.core.player.RandomBot;

public class PlayerFactory {

    public static Player getPlayerByTypeAndSide(final PlayerType type, final Side side) {

        Player player = null;

        switch (type) {
            case RANDOM_BOT:
                player = new RandomBot(side);
                break;
            case CLIENT:
                player = new Client(side);
                break;
            case HUMAN:
                player = new HumanPlayer(side);
                break;
        }
        return player;
    }
}
