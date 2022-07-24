package io.deeplay.server.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.utils.CommandSerializator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class InboundObjectDecoder extends ChannelInboundHandlerAdapter {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(InboundObjectDecoder.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("Подключлся новый клиент");
        logger.info("Ожидаем подтверждения версии протокола");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        try {
            buf.readBytes(bytes);
            Command inputCommand = CommandSerializator.deserializeByteArray(bytes);
            ctx.fireChannelRead(inputCommand);
        } catch (IOException e) {
            logger.error("Ошибка про десериализации входящего сообщение", e);
        } finally {
            buf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Соединение с клиентом прервано", cause);
        ctx.close();
    }
}
