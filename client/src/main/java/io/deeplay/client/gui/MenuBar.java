package io.deeplay.client.gui;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    private final Runnable sendNewGameRequest;
    public MenuBar(final Runnable sendNewGameRequest) {
        add(createFileMenu());
        this.sendNewGameRequest = sendNewGameRequest;
    }

    public JMenu createFileMenu() {
        JMenu file = new JMenu("Файл");
        JMenuItem newGame = new JMenuItem("Новая игра");
        JMenuItem exit = new JMenuItem("Exit");
        file.add(newGame);
        file.addSeparator();
        file.add(exit);
        newGame.addActionListener(e -> sendNewGameRequest.run());
        exit.addActionListener(e -> System.exit(0));
        return file;
    }
}
