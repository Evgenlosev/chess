package io.deeplay.client.nettyClient.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.clientToServer.ProtocolVersionRequest;
import io.deeplay.interaction.utils.CommandSerializator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClientInboundObjectDecoder extends ChannelInboundHandlerAdapter {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ClientInboundObjectDecoder.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("Успешно подключено к серверу");
        ctx.writeAndFlush(new ProtocolVersionRequest("1.0"));
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
        logger.error("Отключено от сервера", cause);
    }
}
