package io.deeplay.client.gui;

import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import java.io.IOException;
import java.util.function.Consumer;

public class Gui extends JFrame {
    // these are the components we need.
    private final JSplitPane splitPane;  // split the window in top and bottom
    private final ChessBoard chessBoard;       // container panel for the top
    private final JPanel moveHistory;    // container panel for the bottom
    private final JScrollPane scrollPane; // makes the text scrollable
    private final JTextArea textArea;     // the text
    private final JPanel inputPanel;      // under the text a container for all the input elements
    private final JTextField textField;   // a textField for the text the user inputs
    private final JButton nextMove;
    private final JButton prevMove;
    private final JMenuBar menubar;
    private final MoveHistory moveHistoryText;
    private GameInfo gameInfo;

    public Gui(final GameInfo gameInfo, final Consumer<MoveInfo> sendMove, final Runnable sendNewGameRequest) {
        super("Chess");
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        menubar = new MenuBar(sendNewGameRequest);

        // first, lets create the containers:
        // the splitPane devides the window in two components (here: top and bottom)
        // users can then move the devider and decide how much of the top component
        // and how much of the bottom component they want to see.
        splitPane = new JSplitPane();

        try {
            chessBoard = new ChessBoard(sendMove);         // our top component
            chessBoard.updateBoard(io.deeplay.core.model.ChessBoard.DEFAULT_FEN_STRING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        moveHistory = new JPanel();      // our bottom component
        moveHistoryText = new MoveHistory();

        // in our bottom panel we want the text area and the input components
        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);  // this scrollPane is used to make the text area scrollable
        textArea = new JTextArea();      // this text area will be put inside the scrollPane

        // the input components will be put in a separate panel
        inputPanel = new JPanel();
        textField = new JTextField(2);    // first the input field where the user can type his text
        nextMove = new JButton(">");
        prevMove = new JButton("<");

        // now lets define the default size of our window and its layout:
        setPreferredSize(new Dimension(1000, 800));     // let open the window with a default size of 400x400 pixels
        // the contentPane is the container that holds all our components
        getContentPane().setLayout(new GridLayout());  // the default GridLayout is like a grid with 1 column and 1 row,
        // we only add one element to the window itself
        getContentPane().add(splitPane);               // due to the GridLayout, our splitPane will now fill the whole window

        // let configure our splitPane:
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);  // we want it to split the window verticaly
        splitPane.setDividerLocation(700);                    // the initial position of the divider is 200 (our window is 400 pixels high)
        splitPane.setLeftComponent(chessBoard);                  // at the top we want our "topPanel"
        splitPane.setRightComponent(moveHistory);            // and at the bottom we want our "bottomPanel"

        // our topPanel doesn't need anymore for this example. Whatever you want it to contain, you can add it here
        moveHistory.setLayout(new BoxLayout(moveHistory, BoxLayout.Y_AXIS)); // BoxLayout.Y_AXIS will arrange the content vertically
        scrollPane.setViewportView(moveHistoryText);
        moveHistory.add(moveHistoryText);                // then we add the inputPanel to the bottomPanel, so it under the scrollPane / textArea
        moveHistory.add(scrollPane);                // then we add the inputPanel to the bottomPanel, so it under the scrollPane / textArea
        moveHistory.add(textArea);
        textArea.append("Player BLACK joined!\n");
        textArea.append("You won!\n");
        textArea.append("Красиво сделал, брат\n");
        moveHistory.add(inputPanel);
        // let set the maximum size of the inputPanel, so it doesn't get too big when the user resizes the window
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));     // we set the max height to 75 and the max width to (almost) unlimited
        textField.setText("Here could be a text");
        inputPanel.add(prevMove);           // and right the "send" button
        inputPanel.add(nextMove);           // and right the "send" button
        inputPanel.add(createFlipButton());           // and right the "send" button
        setJMenuBar(menubar);

        pack();// calling pack() at the end, will ensure that every layout and size we just defined gets applied before the stuff becomes visible
    }

    public void updateBoard(final GameInfo gameInfo, final MoveInfo moveInfo) {
        chessBoard.updateBoard(gameInfo.getFenBoard());
        moveHistoryText.update(gameInfo, moveInfo);
        pack();
    }

    private JButton createFlipButton() {
        JButton flip = new JButton("flip");
        flip.addActionListener(e -> {
            chessBoard.reverse();
            super.setVisible(true);
        });
        return flip;
    }

    public void restart(final GameInfo gameInfo) {
        moveHistoryText.restart();
        chessBoard.updateBoard(gameInfo.getFenBoard());
    }
}
