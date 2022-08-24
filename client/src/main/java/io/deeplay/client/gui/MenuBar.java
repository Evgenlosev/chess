package io.deeplay.client.gui;

import io.deeplay.core.model.Side;

import javax.swing.*;
import java.util.function.Consumer;

public class MenuBar extends JMenuBar {
    private final Consumer<Side> sendNewGameRequest;
    public MenuBar(final Consumer<Side> sendNewGameRequest) {
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
        newGame.addActionListener(e -> {
            JRadioButton white = new JRadioButton("Белые");
            JRadioButton black = new JRadioButton("Черные");
            ButtonGroup bg = new ButtonGroup();
            bg.add(white);
            bg.add(black);
            final JComponent[] inputs = new JComponent[] {
                    white,
                    black
            };
            JOptionPane.showConfirmDialog(null, inputs, "Выберите сторону:", JOptionPane.DEFAULT_OPTION);
            if (white.isSelected()) {
                sendNewGameRequest.accept(Side.WHITE);
            } else if (black.isSelected()) {
                sendNewGameRequest.accept(Side.BLACK);
            }
        });
        exit.addActionListener(e -> System.exit(0));
        return file;
    }
}
