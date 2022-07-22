package io.deeplay.client.nettyClient.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.serverToClient.ProtocolVersionResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

public class InboundProtocolVersionResponseHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(InboundProtocolVersionResponseHandler.class);

    /**
     * В этом блоке ожидаем подтверждение версии протокола взаимодействия с сервером.
     * @param ctx
     * @param command в этом блоке получаем на вход десериализованный объект класса Command
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command command) {
        logger.info("Поступила команда от сервера: {}", command);
        if (command instanceof ProtocolVersionResponse) {
            ProtocolVersionResponse pvr = (ProtocolVersionResponse) command;
            if (pvr.isVersionMatch()) {
                logger.info("Версия протокола подтверждена сервером");
                //Если версия протокола подтверждена, удаляем из конвеера текущий хэндлер и добавляем CommandHandler
                ctx.channel().pipeline().remove(this);
                ctx.channel().pipeline().addLast(new ClientInboundCommandHandler());
            } else {
                logger.info("Версия протокола отклонена сервером");
            }
        } else {
            logger.info("Версия протокола отклонена сервером");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Прервано соединение с сервером", cause);
    }
}
