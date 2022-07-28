package io.deeplay.server.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.clientToServer.AuthRequest;
import io.deeplay.interaction.serverToClient.AuthResponse;
import io.deeplay.server.ChessNettyServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

public class AuthorizationHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(AuthorizationHandler.class);

    /**
     * В этом хэндлере ожидаем от клиента AuthRequest
     * @param ctx
     * @param command
     */
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Command command) {
        LOGGER.info("Принята команда от клиента: {}", command);
        if (command instanceof AuthRequest) {
            AuthRequest authRequest = (AuthRequest) command;
            int clientId = ChessNettyServer.addClient(authRequest.getUserName());
            if (clientId != 0) {
                LOGGER.info("Пользователь авторизован с id - {}", clientId);
                ctx.writeAndFlush(new AuthResponse(true, clientId));
                //Если авторизация подтверждена, удаляем из конвеера текущий хэндлер и добавляем CommandHandler
                ctx.channel().pipeline().remove(this);
                ctx.channel().pipeline().addLast(new InboundCommandHandler());
            } else {
                String errorMessage = "Имя пользователя '" + authRequest.getUserName() + "' занято.";
                LOGGER.info("Пользователь не авторизован. " + errorMessage);
                ctx.writeAndFlush(new AuthResponse(false, 0, errorMessage));
            }
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOGGER.error("Соединение с клиентом прервано", cause);
        ctx.close();
    }
}
