package io.deeplay.client.gui;

import io.deeplay.core.model.MoveInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ChessBoard extends JPanel {
    private static final int RED_OFFSET = -10;
    private static final int GREEN_OFFSET = -30;
    private static final int BLUE_OFFSET = -40;
    private static final Color BLACK_CELL = new Color(181, 136, 99);
    private static final Color WHITE_CELL = new Color(240, 217, 181);
    private JButton[] cells;
    private int lastPressedButton;
    private Set<MoveInfo> moves;
    private final Map<String, BufferedImage> figuresFromLetters = Map.ofEntries(
            Map.entry("q", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/bQ.png"))),
            Map.entry("k", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/bK.png"))),
            Map.entry("r", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/bR.png"))),
            Map.entry("n", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/bN.png"))),
            Map.entry("b", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/bB.png"))),
            Map.entry("p", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/bP.png"))),
            Map.entry("Q", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/wQ.png"))),
            Map.entry("K", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/wK.png"))),
            Map.entry("R", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/wR.png"))),
            Map.entry("N", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/wN.png"))),
            Map.entry("B", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/wB.png"))),
            Map.entry("P", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/wP.png"))),
            Map.entry("1", ImageIO.read(new File("client/src/main/resources/ChessPiecesPNG/none.png")))
    );

    public ChessBoard() throws IOException {
        super();
        this.setLayout(new GridLayout(8, 8));
        cells = new JButton[64];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i * 8 + j] = new JButton();
                cells[i].setBorderPainted(false);
                if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
                    cells[i * 8 + j].setBackground(WHITE_CELL);
                } else {
                    cells[i * 8 + j].setBackground(BLACK_CELL);
                }
                cells[i * 8 + j].setName(String.valueOf((7 - i) * 8 + j));
                cells[i * 8 + j].addActionListener(this::processClick);
            }
        }
        for (JButton btn : cells) {
            this.add(btn);
        }
    }

    public void updateBoard(final String fen, final Set<MoveInfo> moves) {
        this.moves = moves;
        String unzippedFen = io.deeplay.core.model.ChessBoard.unzipFen(fen).split(" ", 2)[0]
                .replace("/", "");

        for (int i = 0; i < unzippedFen.length(); i++) {
            cells[i].setIcon(new ImageIcon(figuresFromLetters.get(unzippedFen.substring(i, i + 1))));
        }

        reverse();
    }

    private void processClick(ActionEvent event) {
        reverse();
        if (((JButton) event.getSource()).getBackground() == BLACK_CELL ||
                ((JButton) event.getSource()).getBackground() == WHITE_CELL) {
            paintPossibleMoves(event);
        } else {
            processMove(lastPressedButton, Integer.parseInt(((JButton) event.getSource()).getName()));
        }
    }

    private void paintPossibleMoves(ActionEvent event) {
        String btnName = ((JButton) event.getSource()).getName();
        restoreBasicColor();
        lastPressedButton = Integer.parseInt(btnName);
        java.util.Set<MoveInfo> movesToHighlight = moves.stream().filter(move ->
                (move.getCellFrom().getIndexAsOneDimension() == lastPressedButton)).collect(Collectors.toSet());

        for (JButton cell : cells) {
            if (movesToHighlight.stream().anyMatch(move ->
                    (move.getCellTo().getIndexAsOneDimension() == Integer.parseInt(cell.getName())))) {
                cell.setBackground(new Color(cell.getBackground().getRed() + RED_OFFSET,
                        cell.getBackground().getGreen() + GREEN_OFFSET,
                        cell.getBackground().getBlue() + BLUE_OFFSET));
            }
        }
    }

    private void restoreBasicColor() {
        for (JButton cell : cells) {
            if (Integer.parseInt(cell.getName()) / 8 % 2 == 1 && Integer.parseInt(cell.getName()) % 2 == 1 ||
                    Integer.parseInt(cell.getName()) / 8 % 2 == 0 && Integer.parseInt(cell.getName()) % 2 == 0) {
                cell.setBackground(BLACK_CELL);
            } else {
                cell.setBackground(WHITE_CELL);
            }
        }
    }

    private void processMove(final int from, final int to) {
        Optional<MoveInfo> move = moves.stream().filter(x ->
                (x.getCellFrom().getIndexAsOneDimension() == from &&
                        x.getCellTo().getIndexAsOneDimension() == to)).findAny();
        if (move.isPresent()) {
            // TODO:: processMove
            throw new RuntimeException("Trying to process move " + move.get());
        }
    }
    private void reverse() {
        java.util.List<JButton> tempCells = new ArrayList<>(List.of(cells));
        Collections.reverse(tempCells);
        cells = tempCells.toArray(new JButton[64]);
        this.removeAll();
        for (JButton cell : cells) {
            this.add(cell);
        }
    }
}
