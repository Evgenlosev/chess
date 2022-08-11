package io.deeplay.core;

import io.deeplay.core.model.Side;
import io.deeplay.core.player.vladbot.EvaluationBot;

//   mvn clean compile exec:java
public class Main {
    public static void main(String[] args) throws InterruptedException {
        SelfPlay selfPlay = new SelfPlay(
                new EvaluationBot(Side.WHITE), new EvaluationBot(Side.BLACK));
        selfPlay.play();
    }



}