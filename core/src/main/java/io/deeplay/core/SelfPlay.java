package io.deeplay.core;

import io.deeplay.core.listener.GameInfoGroup;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelfPlay {
    private static final Logger LOGGER = LoggerFactory.getLogger(SelfPlay.class);
    private final Player firstPlayer;
    private final Player secondPlayer;
    private final GameInfo gameInfo;
    private final GameInfoGroup gameInfoGroup;
    private Player currentPlayerToMove;

    public SelfPlay(final Player firstPlayer, final Player secondPlayer) {
        if (firstPlayer.getSide() == secondPlayer.getSide()) {
            throw new IllegalArgumentException("Соперники не могут играть одним цветом");
        }
        this.gameInfo = new GameInfo();
        if (firstPlayer.getSide() == Side.WHITE) {
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
        } else {
            this.firstPlayer = secondPlayer;
            this.secondPlayer = firstPlayer;
        }
        this.currentPlayerToMove = firstPlayer;
        this.gameInfoGroup = new GameInfoGroup(gameInfo);
    }

    /**
     * Метод переключает текущего игрока, чей ход ожидается
     */
    private void changeCurrentPlayerToMove() {
        if (currentPlayerToMove.getSide() == firstPlayer.getSide()) {
            currentPlayerToMove = secondPlayer;
            return;
        }
        currentPlayerToMove = firstPlayer;
    }

    public void play() {
        gameInfoGroup.playerSeated(firstPlayer.getSide());
        LOGGER.info("Игрок присоединился к партии за белых");
        gameInfoGroup.playerSeated(secondPlayer.getSide());
        LOGGER.info("Игрок присоединился к партии за черных");
        gameInfoGroup.gameStarted();
        LOGGER.info("Партия началась");
        //Пока игра не закончена, рассылаем всем слушателям ходы игроков
        while (gameInfo.isGameOver()) {
            LOGGER.info("Ходят {}", currentPlayerToMove.getSide());
            final MoveInfo moveInfo = currentPlayerToMove.getAnswer(gameInfo);
            gameInfoGroup.playerActed(currentPlayerToMove.getSide(), moveInfo);
            LOGGER.info("{} совершили ход: {}", currentPlayerToMove.getSide(), moveInfo);
            LOGGER.info("\n" + gameInfo.getBoard().toString());
            changeCurrentPlayerToMove();
        }
        gameInfoGroup.gameOver();
        LOGGER.info("Игра закончена. {}", gameInfo.getGameStatus().getMessage());
    }
}
