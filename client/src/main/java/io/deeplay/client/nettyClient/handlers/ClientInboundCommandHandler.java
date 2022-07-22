package io.deeplay.client.nettyClient.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.interaction.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

public class ClientInboundCommandHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ClientInboundCommandHandler.class);

    /**
     * @param channelHandlerContext ссылка на контекст между сервером  клиеном
     * @param command в этом блоке получаем на вход десериализованный объект класса Command
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) {
        logger.info("Поступила команда от сервера: {}", command);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Прервано соединение с сервером", cause);
    }
}
