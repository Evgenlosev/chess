package io.deeplay.client.nettyClient.handlers;

import ch.qos.logback.classic.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InboundClientHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(InboundClientHandler.class);
    @Override
    //Обработка события: подключение нового клиента
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        logger.info("Подключились к серверу");

        new Thread(() -> {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String text = br.readLine();
                    ctx.writeAndFlush(text);
                    logger.info("Отправлено сообщение серверу: " + text);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    @Override
    //Обработка события: входящее сообщение от клиента
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        logger.info("Получено сообщение от клиента: " + s);
    }

    @Override
    //Перехватчик исключений
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Ошибка:", cause);
        ctx.close();
    }
}
