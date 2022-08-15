package io.deeplay.client.ui;

import io.deeplay.core.console.BoardDrawer;
import io.deeplay.core.logic.BitUtils;
import io.deeplay.core.model.*;
import io.deeplay.interaction.clientToServer.StartGameRequest;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;
import java.util.Scanner;

/**
 * Консольная UI
 */
public class TUI implements UI {

    Scanner scanner = new Scanner(System.in);
    String userInput;
    Optional<MoveInfo> userMove;

    public TUI() {
    }

    @Override
    public void updateBoard(final GameInfo gameInfo) {
        BoardDrawer.draw(gameInfo.getFenBoard());
    }

    @Override
    public MoveInfo getMove(final GameInfo gameInfo) {
        while(true) {
            System.out.println("Make your move! (e2e4/E2 e4/E2E4 formats available)");
            userInput = scanner.nextLine().trim().toUpperCase().replace(" ", "");
            if ("QUIT".equals(userInput)) {
                System.exit(0);
            }
            String moveRegex = "[A-H][1-8][A-H][1-8]";
            if (!userInput.matches(moveRegex)) {
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

    @Override
    public void gameOver(final GameStatus gameStatus) {
        System.out.println("Игра закончена. " + gameStatus.getMessage());
        while (true) {
            System.out.println("Хотите сыграть еще раз: \n 1 - да \n 2 - нет");
            userInput = scanner.nextLine();
            if (userInput.equals("1")) {
                break;
            }
            if (userInput.equals("2")) {
                System.exit(0);
            }
            System.out.println("Некорректный ввод");
        }
    }

    @Override
    public StartGameRequest getGameSettings() {
        Side side;
        String enemyType;
        while (true) {
            System.out.println("Выберите цвет, введите: \n 1 - белый \n 2 - черный");
            userInput = scanner.nextLine();
            if (userInput.equals("1")) {
                side = Side.WHITE;
                break;
            }
            if (userInput.equals("2")) {
                side = Side.BLACK;
                break;
            }
            System.out.println("Некорректный ввод");
        }
        while (true) {
            System.out.println("Выберите тип соперника, введите: \n 1 - рандомный бот");
            userInput = scanner.nextLine();
            if (userInput.equals("1")) {
                enemyType = "RandomBot";
                break;
            }
            System.out.println("Некорректный ввод");
        }
        return new StartGameRequest(side, enemyType);
    }

    @Override
    public void start(final GameInfo gameInfo) {
        BoardDrawer.draw(gameInfo.getFenBoard());
    }
}
