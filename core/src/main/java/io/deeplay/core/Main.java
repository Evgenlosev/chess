package io.deeplay.core;

import io.deeplay.core.console.BoardDrawer;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.RandomBot;

//   mvn clean compile exec:java
public class Main {
    public static void main(final String[] args) {
        SelfPlay selfPlay = new SelfPlay(new RandomBot(Side.WHITE), new RandomBot(Side.BLACK));
        selfPlay.play();

    }



}