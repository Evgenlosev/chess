package io.deeplay.server.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.clientToServer.ProtocolVersionRequest;
import io.deeplay.interaction.serverToClient.ProtocolVersionResponse;
import io.deeplay.server.ChessNettyServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

public class ProtocolVersionHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ProtocolVersionHandler.class);

    /**
     * В этом хэндлере ожидаем от клиента ProtocolVersionRequest
     * @param ctx
     * @param command
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command command) {
        logger.info("Принята команда от клиента: {}", command);
        if (command instanceof ProtocolVersionRequest) {
            ProtocolVersionRequest pvr = (ProtocolVersionRequest) command;
            if (ChessNettyServer.checkProtocolVersion(pvr.getProtocolVersion())) {
                logger.info("Версия протокола подтверждена");
                ctx.writeAndFlush(new ProtocolVersionResponse(true));
                //Если версия протокола подтверждена, удаляем из конвеера текущий хэндлер и добавляем CommandHandler
                ctx.channel().pipeline().remove(this);
                ctx.channel().pipeline().addLast(new InboundCommandHandler());
            } else {
                logger.info("Версия протокола отклонена");
                ctx.writeAndFlush(
                        new ProtocolVersionResponse(false, "Версия протокола отклонена"));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Соединение с клиентом прервано", cause);
    }
}
