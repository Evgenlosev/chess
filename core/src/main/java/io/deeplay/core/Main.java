package io.deeplay.core;

import io.deeplay.core.console.BoardDrawer;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.RandomBot;

//   mvn clean compile exec:java
public class Main {
    public static void main(String[] args) {
        BoardDrawer.draw(null);
        SelfPlay selfPlay = new SelfPlay(new RandomBot(Side.WHITE, 0), new RandomBot(Side.BLACK, 1));
        selfPlay.play();

    }



}