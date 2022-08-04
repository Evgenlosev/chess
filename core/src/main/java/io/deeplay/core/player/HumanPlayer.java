package io.deeplay.core.player;


import io.deeplay.core.console.ConsoleCommands;
import io.deeplay.core.logic.BitUtils;
import io.deeplay.core.model.Coord;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;
import java.util.Scanner;

public class HumanPlayer extends Player {
    //TODO: UI на вход принимает 2 координаты: от и куда. Нужен какой-то валидатор, который определит фигуру,
    // тип хода (MoveType) и вернет объект MoveInfo.

    Scanner scanner = new Scanner(System.in);
    String userInput;
    Optional<MoveInfo> userMove;
    private final String moveRegex = "[A-H][1-8][A-H][1-8]";
    // Функция для того, чтобы передавать ход от пользователя в selfplay.

    public HumanPlayer(final Side side) {
        super(side);
    }
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        while (true){
            System.out.println("Make your move! (e2e4/E2 e4/E2E4 formats available)");
            userInput = scanner.nextLine().trim().toUpperCase().replace(" ", "");
            if (!userInput.matches(moveRegex)){
                System.out.print("\033[2F\033[J");
                continue;
            }
            Coord coordFrom = new Coord(ArrayUtils.indexOf(BitUtils.SQUARES_STRING, userInput.substring(0, 2)));
            Coord coordTo = new Coord(ArrayUtils.indexOf(BitUtils.SQUARES_STRING, userInput.substring(2, 4)));
            if ((userMove = gameInfo.getAvailableMoves().stream().filter(move -> move.getCellFrom().equals(coordFrom) && move.getCellTo().equals(coordTo)).findFirst()).isPresent()) {
                return userMove.get();
            } else {
                System.out.print("\033[2F\033[J");
                System.out.println("Ход " + userInput + " невозможен");
            }
        }
    }
}
