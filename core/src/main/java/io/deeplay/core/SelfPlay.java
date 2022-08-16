package io.deeplay.core;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.listener.GameInfoGroup;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;
import io.deeplay.core.statistics.AllGamesStatistics;
import org.slf4j.LoggerFactory;


public class SelfPlay {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(SelfPlay.class);
    private final Player firstPlayer;
    private final Player secondPlayer;
    private final GameInfo gameInfo;
    private final GameInfoGroup gameInfoGroup;
    private Player currentPlayerToMove;

    /**
     * Количество партий, которые оба игрока будут играть подряд(не меняя стороны).
     */
    private final int gamesAmount;

    public SelfPlay(final Player firstPlayer,
                    final Player secondPlayer,
                    final GameInfo gameInfo,
                    final int gamesAmount,
                    final boolean gatherStatistics) {
        if (firstPlayer.getSide() == secondPlayer.getSide()) {
            throw new IllegalArgumentException("Соперники не могут играть одним цветом");
        }
        this.gameInfo = gameInfo;
        if (firstPlayer.getSide() == Side.WHITE) {
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
        } else {
            this.firstPlayer = secondPlayer;
            this.secondPlayer = firstPlayer;
        }
        this.currentPlayerToMove = this.firstPlayer;
        this.gameInfoGroup = new GameInfoGroup(gameInfo);
        gameInfoGroup.addListener(firstPlayer);
        gameInfoGroup.addListener(secondPlayer);
        this.gamesAmount = gamesAmount;
        if (gatherStatistics) {
            gameInfoGroup.addListener(new AllGamesStatistics(this));
        }
    }

    public SelfPlay(final Player firstPlayer,
                    final Player secondPlayer,
                    final GameInfo gameInfo) {
        this(firstPlayer, secondPlayer, gameInfo, 1, false);
    }

    public SelfPlay(final Player firstPlayer,
                    final Player secondPlayer,
                    final int gamesAmount,
                    final boolean gatherStatistics) {
        this(firstPlayer, secondPlayer, new GameInfo(), gamesAmount, gatherStatistics);
    }

    public SelfPlay(final Player firstPlayer, final Player secondPlayer) {
        this(firstPlayer, secondPlayer, new GameInfo());
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

    public void play() throws InterruptedException {
        gameInfoGroup.playerSeated(firstPlayer.getSide());
        LOGGER.info("{} присоединился к партии за белых", firstPlayer);
        gameInfoGroup.playerSeated(secondPlayer.getSide());
        LOGGER.info("{} присоединился к партии за черных", secondPlayer);
        int countGamesAmount = 0;
        while (countGamesAmount++ < gamesAmount) {
            gameInfoGroup.gameStarted();
            LOGGER.info("Партия началась");
            //Пока игра не закончена, рассылаем всем слушателям ходы игроков
            while (gameInfo.isGameOver()) {
                // BoardDrawer.draw(gameInfo.getFenBoard());
                LOGGER.info("Ходят {}", currentPlayerToMove.getSide().getDescription());
                final MoveInfo moveInfo = currentPlayerToMove.getAnswer(gameInfo);
                gameInfoGroup.playerActed(currentPlayerToMove.getSide(), moveInfo);
                LOGGER.info("{} совершили ход: {}", currentPlayerToMove.getSide().getDescription(), moveInfo.toString());
                LOGGER.info("\n" + gameInfo.getBoard().toString());
                changeCurrentPlayerToMove();
            }
            // BoardDrawer.draw(gameInfo.getFenBoard());
            gameInfoGroup.gameOver();
            LOGGER.info("Игра закончена. {}", gameInfo.getGameStatus().getMessage());
            if (countGamesAmount < gamesAmount)
                gameInfo.resetGame();
        }
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public int getGamesAmount() {
        return gamesAmount;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }
}
