package io.deeplay.client.nettyClient;

import ch.qos.logback.classic.Logger;
import io.deeplay.client.ChessClient;
import io.deeplay.client.nettyClient.handlers.ClientInboundObjectDecoder;
import io.deeplay.client.nettyClient.handlers.ClientOutBoundCommandEncoder;
import io.deeplay.client.nettyClient.handlers.InboundProtocolVersionResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ChessNettyClient implements ChessClient {
    private final String host;
    private final int port;
    private static final String PROTOCOL_VERSION = "1.1";
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ChessNettyClient.class);

    public ChessNettyClient(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void start() {
        //Пул потоков для клиента
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //Настройки клиента
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ClientOutBoundCommandEncoder(),
                                    new ClientInboundObjectDecoder(),
                                    new InboundProtocolVersionResponseHandler());
                        }
                    });
            //Запуск клиента
            ChannelFuture channelFuture = b.connect().sync();
            //Ожидание завершения работы клиента
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Ошибка при запуске клиента", e);
        } finally {
            try {
                group.shutdownGracefully().sync();
                logger.info("Клиент остановлен");
            } catch (InterruptedException e) {
                logger.error("Ошибка при остановке клиента", e);
            }
        }
    }

    public static String getProtocolVersion() {
        return PROTOCOL_VERSION;
    }
}
