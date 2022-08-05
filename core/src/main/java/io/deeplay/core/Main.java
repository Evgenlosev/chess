package io.deeplay.core;

import io.deeplay.core.model.Side;
import io.deeplay.core.player.HumanPlayer;
import io.deeplay.core.player.RandomBot;

//   mvn clean compile exec:java
public class Main {
    public static void main(String[] args) throws InterruptedException {
//        BoardDrawer.draw(null);
        SelfPlay selfPlay = new SelfPlay(new HumanPlayer(Side.WHITE), new RandomBot(Side.BLACK, 0L));
        selfPlay.play();
//        Scanner scanner = new Scanner(System.in);
//        System.out.println(scanner.next().substring(2, 4));
    }



}