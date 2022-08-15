package io.deeplay.client.gui;

import io.deeplay.core.model.MoveInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Set;
import java.util.function.Consumer;

public class Gui extends JFrame {
    private Consumer<MoveInfo> function;
    // these are the components we need.
    private final JSplitPane splitPane;  // split the window in top and bottom
    private final ChessBoard chessBoard;       // container panel for the top
    private final JPanel moveHistory;    // container panel for the bottom
    private final JScrollPane scrollPane; // makes the text scrollable
    private final JTextArea textArea;     // the text
    private final JPanel inputPanel;      // under the text a container for all the input elements
    private final JTextField textField;   // a textField for the text the user inputs
    private final JButton button;         // and a "send" button

    public Gui(Consumer<MoveInfo> function) {
        super("Chess");
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.function = function;

        // first, lets create the containers:
        // the splitPane devides the window in two components (here: top and bottom)
        // users can then move the devider and decide how much of the top component
        // and how much of the bottom component they want to see.
        splitPane = new JSplitPane();

        try {
            chessBoard = new ChessBoard(function);         // our top component
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        moveHistory = new JPanel();      // our bottom component

        // in our bottom panel we want the text area and the input components
        scrollPane = new JScrollPane();  // this scrollPane is used to make the text area scrollable
        textArea = new JTextArea();      // this text area will be put inside the scrollPane

        // the input components will be put in a separate panel
        inputPanel = new JPanel();
        textField = new JTextField();    // first the input field where the user can type his text
        button = new JButton("send");    // and a button at the right, to send the text

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

        moveHistory.add(scrollPane);                // first we add the scrollPane to the bottomPanel, so it is at the top
        scrollPane.setViewportView(textArea);       // the scrollPane should make the textArea scrollable, so we define the viewport
        moveHistory.add(inputPanel);                // then we add the inputPanel to the bottomPanel, so it under the scrollPane / textArea

        // let set the maximum size of the inputPanel, so it doesn't get too big when the user resizes the window
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));     // we set the max height to 75 and the max width to (almost) unlimited
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));   // X_Axis will arrange the content horizontally

        inputPanel.add(textField);        // left will be the textField
        inputPanel.add(button);           // and right the "send" button

        pack();// calling pack() at the end, will ensure that every layout and size we just defined gets applied before the stuff becomes visible
    }

    public void updateBoard(final String fen, final Set<MoveInfo> moveInfoSet) {
        chessBoard.updateBoard(fen, moveInfoSet);
        super.setVisible(true);
    }
}
