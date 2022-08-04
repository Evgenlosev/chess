package io.deeplay.client.nettyClient.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.interaction.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

public class ClientInboundCommandHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ClientInboundCommandHandler.class);

    /**
     * @param ctx     ссылка на контекст между сервером и клиентом
     * @param command в этом блоке получаем на вход десериализованный объект класса Command
     */
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Command command) {

    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOGGER.error("Прервано соединение с сервером", cause);
        ctx.close();
    }
}
