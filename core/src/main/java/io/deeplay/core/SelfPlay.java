package io.deeplay.core;

import io.deeplay.core.listener.ChessListener;
import io.deeplay.core.listener.GameInfoGroup;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;
import io.deeplay.core.player.PlayerType;
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

        //Если Player является клиентом, добавляем его в список слушателей игровых событий
        if (firstPlayer.getPlayerType() == PlayerType.CLIENT) {
            gameInfoGroup.addListener((ChessListener) firstPlayer);
        }
        if (secondPlayer.getPlayerType() == PlayerType.CLIENT) {
            gameInfoGroup.addListener((ChessListener) secondPlayer);
        }
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
        LOGGER.info("{} присоединился к партии за белых", firstPlayer);
        gameInfoGroup.playerSeated(secondPlayer.getSide());
        LOGGER.info("{} присоединился к партии за черных", secondPlayer);
        gameInfoGroup.gameStarted();
        LOGGER.info("Партия началась");
        //Пока игра не закончена, рассылаем всем слушателям ходы игроков
        while (gameInfo.isGameOver()) {
            LOGGER.info("Ходят {}", currentPlayerToMove.getSide().getDescription());
            final MoveInfo moveInfo = currentPlayerToMove.getAnswer(gameInfo);
            gameInfoGroup.playerActed(currentPlayerToMove.getSide(), moveInfo);
            LOGGER.info("{} совершили ход: {}", currentPlayerToMove.getSide().getDescription(), moveInfo.toString());
            LOGGER.info("\n" + gameInfo.getBoard().toString());
            changeCurrentPlayerToMove();
        }
        gameInfoGroup.gameOver();
        LOGGER.info("Игра закончена. {}", gameInfo.getGameStatus().getMessage());
    }
}
