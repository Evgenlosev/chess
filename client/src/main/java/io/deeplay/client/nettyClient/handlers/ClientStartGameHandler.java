package io.deeplay.client.nettyClient.handlers;

import io.deeplay.client.session.ClientGameSession;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;
import io.deeplay.core.player.RandomBot;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;
import io.deeplay.interaction.clientToServer.StartGameRequest;
import io.deeplay.interaction.serverToClient.StartGameResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * В этом блоке направляем запрос на начало игры с заданными параметрами.
 */
public class ClientStartGameHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientStartGameHandler.class);
    private Side side;

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) {
        // TODO Реализовать получение настроек игры от пользователя
//        new Thread(() -> {
//            StartGameRequest startGameRequest = ui.getGameSettings();
//            side = startGameRequest.getSide();
//            ctx.writeAndFlush(startGameRequest);
//        }).start();
        this.side = Side.WHITE;
        ctx.writeAndFlush(new StartGameRequest(side, "RandomBot"));
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Command command) {
        if (command.getCommandType() == CommandType.START_GAME_RESPONSE) {
            StartGameResponse startGameResponse = (StartGameResponse) command;
            if (startGameResponse.isGameStarted()) {
                LOGGER.info("Начало игры");
                //Создаем и запускаем игровую сессию по параметрам, заданным пользователем
                Player player = new RandomBot(side);
                ClientGameSession session = new ClientGameSession(player, ctx);
                session.start();
                //Если игра создана успешно, удаляем из конвеера текущий хэндлер и добавляем CommandHandler
                ctx.channel().pipeline().remove(this);
                ctx.channel().pipeline().addLast(new ClientInboundCommandHandler(session));
            } else {
                LOGGER.info("Игра не создана {}", startGameResponse.getErrorMessage());
            }
        } else {
            LOGGER.info("Ожидаем от сервера подтверждение создания игры");
        }
    }
}
