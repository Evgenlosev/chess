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
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(InboundObjectDecoder.class);

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        LOGGER.info("Подключился новый клиент");
        LOGGER.info("Ожидаем подтверждения версии протокола");
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        try {
            buf.readBytes(bytes);
            Command inputCommand = CommandSerializator.deserializeByteArray(bytes);
            LOGGER.info("Принята команда от клиента {}", inputCommand);
            ctx.fireChannelRead(inputCommand);
        } catch (IOException e) {
            LOGGER.error("Ошибка про десериализации входящего сообщение", e);
        } finally {
            buf.release();
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOGGER.error("Соединение с клиентом прервано", cause);
        ctx.close();
    }
}
