package io.deeplay.server.handlers;

import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;
import io.deeplay.core.player.PlayerFactory;
import io.deeplay.core.player.PlayerType;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;
import io.deeplay.interaction.clientToServer.StartGameRequest;
import io.deeplay.server.session.AwaitSessionStorage;
import io.deeplay.server.session.GameSession;
import io.deeplay.server.client.Client;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * В этом хэндлере ожидаем от клиента StartGameRequest
 */
public class StartGameHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartGameHandler.class);

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Command command) {
        if (command.getCommandType() == CommandType.START_GAME_REQUEST) {
            StartGameRequest startGameRequest = (StartGameRequest) command;
            Client client = new Client(startGameRequest.getSide(), ctx);
            GameSession gameSession;

            //Если клиент хочет сыграть против человека
            if (startGameRequest.getEnemyType() == PlayerType.HUMAN) {
                if (AwaitSessionStorage.isEmpty()) {
                    gameSession = new GameSession(client);
                    AwaitSessionStorage.add(gameSession);
                    LOGGER.info("В партии {} ожидается подключение оппонента.", gameSession.getSessionToken());
                } else {
                    gameSession = AwaitSessionStorage.getAwaitSession();
                    AwaitSessionStorage.remove(gameSession);
                    client.setSide(Side.otherSide(gameSession.getFirstPlayer().getSide()));
                    gameSession.setSecondPlayer(client);
                    LOGGER.info("Начало партии {}.", gameSession.getSessionToken());
                    gameSession.start();
                }
            } else {
                Player enemy = PlayerFactory.createPlayer(startGameRequest.getEnemyType(),
                        Side.otherSide(startGameRequest.getSide()));
                gameSession = new GameSession(client, enemy);
                LOGGER.info("Начало партии {}.", gameSession.getSessionToken());
                gameSession.start();
            }

            //Если сессия создана, удаляем из конвейера текущий хэндлер и добавляем CommandHandler
            ctx.channel().pipeline().remove(this);
            ctx.channel().pipeline().addLast(new InboundCommandHandler(client, gameSession));
        }
    }
}
