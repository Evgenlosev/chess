package io.deeplay.client.gui;

import io.deeplay.core.api.SimpleLogic;
import io.deeplay.core.api.SimpleLogicAppeal;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    public static void main(String[] args) throws IOException {
        SimpleLogicAppeal logic = new SimpleLogic();
        ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
        String fen = "rnbqkbnr/ppp2ppp/3p4/4p2Q/4P3/8/PPPP1PPP/RNB1KBNR w KQkq - 0 3";
        Gui gui = new Gui();
        gui.setVisible(true);
        gui.updateBoard(fen, logic.getMoves(fen));
//        gui.setVisible(true);

    }
}
