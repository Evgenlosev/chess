package io.deeplay.server.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.utils.CommandSerializator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OutBoundCommandEncoder extends ChannelOutboundHandlerAdapter {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(OutBoundCommandEncoder.class);

    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
        Command command = (Command) msg;
        ByteBuf buf = ctx.alloc().directBuffer();
        try {
            byte[] bytes = CommandSerializator.serializeCommand(command);
            buf.writeBytes(bytes);
            ctx.writeAndFlush(buf);
            LOGGER.info("Отправлена команда клиенту: {}", command);
        } catch (IOException e) {
            LOGGER.error("Ошибка при сериализации комманды", e);
        } finally {
            buf.release();
        }
    }

}
