package io.deeplay.core;

import io.deeplay.core.model.Side;
import io.deeplay.core.player.RandomBot;

public class Main {
    public static void main(final String[] args) {
        SelfPlay selfPlay = new SelfPlay(new RandomBot(Side.WHITE, 0), new RandomBot(Side.BLACK, 1));
        selfPlay.play();
    }
}