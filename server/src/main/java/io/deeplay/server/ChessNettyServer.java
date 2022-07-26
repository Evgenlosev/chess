package io.deeplay.server;

import io.deeplay.server.handlers.InboundObjectDecoder;
import io.deeplay.server.handlers.OutBoundCommandEncoder;
import io.deeplay.server.handlers.ProtocolVersionHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

import java.util.HashMap;
import java.util.Map;

public class ChessNettyServer {
    private static final int PORT = 8189;
    private static final String PROTOCOL_VERSION = "1.0";
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ChessNettyServer.class);
    private static final Map<Integer, String> activeClients = new HashMap<>();
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
                        public void initChannel(SocketChannel ch) {
                            // настройка конвеера для каждого подключившегося клиента
                            ch.pipeline().addLast(
                                    new OutBoundCommandEncoder(),
                                    new InboundObjectDecoder(),
                                    new ProtocolVersionHandler());
                        }
                    });
            //Запуск сервера
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            logger.info("Сервер запущен на порту " + PORT);
            //Ожидание завершения работы сервера
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully(); //Останавливаем потоки подключения клиентов
            workerGroup.shutdownGracefully(); //Останавливаем потоки обработки сообщений
            logger.info("Сервер остановлен");
        }
    }

    public static boolean checkProtocolVersion(String version) {
        return PROTOCOL_VERSION.equals(version);
    }

    public static String getProtocolVersion() {
        return PROTOCOL_VERSION;
    }

    /**
     * Добавляем пользователя в список activeClients
     * @param userName
     * @return
     * clientId - если имя успешно добавлено.
     * 0 - если пользователь с таким именем уже есть в списке.
     */
    public static Integer addClient(String userName) {
        if (activeClients.size() == 0) {
            activeClients.put(1, userName);
            return 1;
        }
        if (activeClients.containsValue(userName)) {
            return 0;
        }
        activeClients.put(activeClients.size() + 1, userName);
        return activeClients.size();
    }

    public static void main(String[] args) throws Exception {
        new ChessNettyServer().run();

    }
}
