package io.deeplay.server.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.clientToServer.MoveRequest;
import io.deeplay.server.client.Client;
import io.deeplay.interaction.CommandType;
import io.deeplay.interaction.clientToServer.GameOverRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;


public class InboundCommandHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(InboundCommandHandler.class);
    private Client client;

    public InboundCommandHandler(final Client client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Command command) {
        LOGGER.info("Принята команда от клиента: {}", command);
        switch (command.getCommandType()) {
            case MOVE_REQUEST:
                MoveRequest moveRequest = (MoveRequest) command;
                MoveInfo moveInfo = moveRequest.getMoveInfo();
                synchronized (client.getMonitor()) {
                    client.setCurrentMove(moveInfo);
                }
                break;
            gameOverHandler(channelHandlerContext, command);
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOGGER.error("Соединение с клиентом прервано", cause);
        ctx.close();
    }

    private void gameOverHandler(final ChannelHandlerContext ctx, final Command command) { // Как отправить ответ обоим игрокам?
        if (command.getCommandType() == CommandType.GAME_OVER_REQUEST) {
            GameOverRequest gameOverRequest = (GameOverRequest) command;
            if (gameOverRequest.getSide() != null) {
                // По хорошему надо получить клиентов, и если сторона из запроса не равна одному из клиентов, то тот и победил
                //GameSession thisGame = new GameSession(client, new RandomBot(Side.otherSide(client.getSide())));
                //LOGGER.info("Начало партии");
                //ctx.writeAndFlush(new StartGameResponse(true));
                //Если сессия создана, удаляем из конвейера текущий хэндлер и добавляем StartGameHandler
                ctx.channel().pipeline().remove(this);
                ctx.channel().pipeline().addLast(new StartGameHandler()); // Пересоздаем игру
            }
            // Если все же null, то мы не знаем какая сторона вышла, кто победил
            // Ping должен предупредить, что определенный клиент пропал и тогда статус игры
            // с победой оставшегося игрока надо передать только одному (ну либо выйдет время на ход)
        }
    }
}
