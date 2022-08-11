package io.deeplay.server;

import io.deeplay.server.handlers.InboundObjectDecoder;
import io.deeplay.server.handlers.OutBoundCommandEncoder;
import io.deeplay.server.handlers.PingPongHandler;
import io.deeplay.server.handlers.ProtocolVersionHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChessNettyServer {
    private static final int PORT = 8189;
    private static final String PROTOCOL_VERSION = "1.0";
    private static final Logger LOGGER = LoggerFactory.getLogger(ChessNettyServer.class);
    private static final Map<Integer, String> ACTIVE_CLIENTS = new HashMap<>();
    public void run() throws Exception {
        //Пул потоков для обработки подключений клиентов
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //Пул потоков для обработки сетевых сообщений
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //Настройки сервера
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // указание канала для подключения новых клиентов
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(final SocketChannel ch) {
                            // настройка конвейера для каждого подключившегося клиента
                            ch.pipeline().addLast(
                                    new IdleStateHandler(60, 30, 0, TimeUnit.SECONDS),
                                    new OutBoundCommandEncoder(),
                                    new InboundObjectDecoder(),
                                    new PingPongHandler(),
                                    new ProtocolVersionHandler());
                        }
                    });
            //Запуск сервера
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            LOGGER.info("Сервер запущен на порту " + PORT);
            //Ожидание завершения работы сервера
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully(); //Останавливаем потоки подключения клиентов
            workerGroup.shutdownGracefully(); //Останавливаем потоки обработки сообщений
            LOGGER.info("Сервер остановлен");
        }
    }

    public static boolean checkProtocolVersion(final String version) {
        return PROTOCOL_VERSION.equals(version);
    }

    public static String getProtocolVersion() {
        return PROTOCOL_VERSION;
    }

    /**
     * Добавляем пользователя в список activeClients
     * @param userName - имя пользователя
     * @return
     * clientId - если имя успешно добавлено.
     * 0 - если пользователь с таким именем уже есть в списке.
     */
    public static Integer addClient(final String userName) {
        if (ACTIVE_CLIENTS.size() == 0) {
            ACTIVE_CLIENTS.put(1, userName);
            return 1;
        }
        if (ACTIVE_CLIENTS.containsValue(userName)) {
            return 0;
        }
        ACTIVE_CLIENTS.put(ACTIVE_CLIENTS.size() + 1, userName);
        return ACTIVE_CLIENTS.size();
    }

    public static void main(final String[] args) throws Exception {
        new ChessNettyServer().run();

    }
}
