package io.deeplay.client.session;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.console.BoardDrawer;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.player.Player;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.clientToServer.MoveRequest;
import io.deeplay.interaction.serverToClient.GameOverResponse;
import io.deeplay.interaction.serverToClient.MoveResponse;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.LoggerFactory;


public class ClientGameSession {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ClientGameSession.class);
    private final Player player;

    private final GameInfo gameInfo;

    private final ChannelHandlerContext ctx;

    public ClientGameSession(final Player player, final ChannelHandlerContext ctx) {
        this.player = player;
        this.ctx = ctx;
        this.gameInfo = new GameInfo();
    }

    public void start() {
        BoardDrawer.draw(gameInfo.getFenBoard());
    }

    public void acceptCommand(Command command) {
        switch (command.getCommandType()) {
            case GET_ANSWER:
                makeMove();
                break;
            case MOVE_RESPONSE:
                MoveResponse moveResponse = (MoveResponse) command;
                updateBoard(moveResponse.getMoveInfo());
                break;
            case GAME_OVER_RESPONSE:
                GameOverResponse gameOverResponse = (GameOverResponse) command;
                LOGGER.info("Игра завершена: {}", gameOverResponse.getGameStatus().getMessage());
                break;
        }
    }

    public void updateBoard(MoveInfo moveInfo) {
        gameInfo.updateBoard(moveInfo);
        BoardDrawer.draw(gameInfo.getFenBoard());
    }

    private void makeMove() {
        if (gameInfo.whoseMove() == player.getSide()) {
            MoveInfo currentMove = player.getAnswer(gameInfo);
            ctx.writeAndFlush(new MoveRequest(currentMove));
        }
    }
}
