package io.deeplay.client.nettyClient.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.model.Side;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;
import io.deeplay.interaction.clientToServer.AuthRequest;
import io.deeplay.interaction.serverToClient.AuthResponse;
import io.deeplay.interaction.serverToClient.ProtocolVersionResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

/**
 * В этом блоке направляем запрос на авторизацию. Идентификатором пользователя является сторона.
 */
public class ClientAuthHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ClientAuthHandler.class);

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) {
        //TODO с GUI или TUI запросить у пользователя цвет фигур, которыми он будет играть
        ctx.writeAndFlush(new AuthRequest(Side.WHITE));
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Command command) {
        if (command.getCommandType() == CommandType.AUTH_RESPONSE) {
            AuthResponse authResponse = (AuthResponse) command;
            if (authResponse.isAuthorized()) {
                LOGGER.info("Успешно авторизованы на сервере");
                //Если авторизация прошла успешно, удаляем из конвеера текущий хэндлер и добавляем ClientStartGameHandler
                ctx.channel().pipeline().remove(this);
                ctx.channel().pipeline().addLast(new ClientStartGameHandler());
            } else {
                LOGGER.info("Авторизация не пройдена", authResponse.getErrorMessage());
            }
        } else {
            LOGGER.info("Ожидаем от сервера подтверждения авторизации");
        }
    }
}
