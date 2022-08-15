package io.deeplay.client.session;

import ch.qos.logback.classic.Logger;
import io.deeplay.client.nettyClient.handlers.ClientInboundCommandHandler;
import io.deeplay.client.nettyClient.handlers.ClientStartGameHandler;
import io.deeplay.client.ui.UI;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.GameStatus;
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

    private final UI ui;

    private final ChannelHandlerContext ctx;

    public ClientGameSession(final Player player, final ChannelHandlerContext ctx, final UI ui) {
        this.player = player;
        this.ctx = ctx;
        this.gameInfo = new GameInfo();
        this.ui = ui;
    }

    public void start() {
        ui.start(gameInfo);
    }

    public void acceptCommand(final Command command) {
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
                gameOver(gameOverResponse.getGameStatus());
                break;
            default:
                throw new RuntimeException("Invalid command is executed: " + command);
        }
    }

    public void updateBoard(final MoveInfo moveInfo) {
        gameInfo.updateBoard(moveInfo);
        ui.updateBoard(gameInfo);
    }

    private void makeMove() {
        if (gameInfo.whoseMove() == player.getSide()) {
            MoveInfo currentMove = player.getAnswer(gameInfo);
            ctx.writeAndFlush(new MoveRequest(currentMove));
        }
    }

    private void gameOver(final GameStatus gameStatus) {
        ui.gameOver(gameStatus);
        ctx.channel().pipeline().remove(ClientInboundCommandHandler.class);
        ctx.channel().pipeline().addLast(new ClientStartGameHandler(ui));
    }
}
