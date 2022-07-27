package io.deeplay.client.nettyClient.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.serverToClient.ProtocolVersionResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

public class InboundProtocolVersionResponseHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(InboundProtocolVersionResponseHandler.class);

    /**
     * В этом блоке ожидаем подтверждение версии протокола взаимодействия с сервером.
     * @param ctx
     * @param command в этом блоке получаем на вход десериализованный объект класса Command
     */
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Command command) {
        LOGGER.info("Поступила команда от сервера: {}", command);
        if (command instanceof ProtocolVersionResponse) {
            ProtocolVersionResponse pvr = (ProtocolVersionResponse) command;
            if (pvr.isVersionMatch()) {
                LOGGER.info("Версия протокола подтверждена сервером");
                //Если версия протокола подтверждена, удаляем из конвеера текущий хэндлер и добавляем CommandHandler
                ctx.channel().pipeline().remove(this);
                ctx.channel().pipeline().addLast(new ClientInboundCommandHandler());
            } else {
                LOGGER.info("Версия протокола отклонена сервером");
            }
        } else {
            LOGGER.info("Ожидаем от сервера подтверждения версии протокола");
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOGGER.error("Прервано соединение с сервером", cause);
        ctx.channel();
    }
}
