package io.deeplay.server.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.interaction.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

public class InboundCommandHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(InboundCommandHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) {
        logger.info("Принята команда от клиента: {}", command);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Соединение с клиентом прервано", cause);
    }
}
