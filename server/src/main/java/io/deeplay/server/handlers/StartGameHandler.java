package io.deeplay.server.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;
import io.deeplay.core.player.RandomBot;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;
import io.deeplay.interaction.clientToServer.AuthRequest;
import io.deeplay.interaction.clientToServer.StartGameRequest;
import io.deeplay.interaction.serverToClient.AuthResponse;
import io.deeplay.interaction.serverToClient.StartGameResponse;
import io.deeplay.server.session.GameSession;
import io.deeplay.server.session.RemotePlayer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

/**
 * В этом хэндлере ожидаем от клиента StartGameRequest
 */
public class StartGameHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(StartGameHandler.class);
    private final Player client;

    public StartGameHandler(final Player client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Command command) {
        if (command.getCommandType() == CommandType.START_GAME_REQUEST) {
            StartGameRequest startGameRequest = (StartGameRequest) command;
            if (startGameRequest.getEnemyPlayerType() != null) {
                //TODO сервер создает соперника того типа, которого запросил клиент
                GameSession thisGame = new GameSession(client, new RandomBot(Side.otherSide(client.getSide())));
                LOGGER.info("Начало партии");
                ctx.writeAndFlush(new StartGameResponse(true));
                //Если сессия создана, удаляем из конвеера текущий хэндлер и добавляем CommandHandler
                ctx.channel().pipeline().remove(this);
                ctx.channel().pipeline().addLast(new InboundCommandHandler());
            } else {
                LOGGER.info("Игра не создана.");
                ctx.writeAndFlush(new StartGameResponse(false,
                        "Не удалось создать игру по заданным параметрам."));
            }
        }
    }
}
