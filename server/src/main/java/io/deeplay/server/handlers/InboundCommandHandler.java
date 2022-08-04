package io.deeplay.server.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;
import io.deeplay.interaction.clientToServer.GameOverRequest;
import io.deeplay.interaction.clientToServer.MoveRequest;
import io.deeplay.server.client.Client;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;


public class InboundCommandHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(InboundCommandHandler.class);
    private final Client client;
    // TODO: gameInfo с chessBoard нужен здесь


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
            case GAME_OVER_REQUEST:
                resignHandler(ctx, command);
                break;
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOGGER.error("Соединение с клиентом прервано", cause);
        ctx.close();
    }

    // Метод обработки события когда игрок сдался
    private void resignHandler(final ChannelHandlerContext ctx, final Command command) { // Как отправить ответ обоим игрокам?
        if (command.getCommandType() == CommandType.GAME_OVER_REQUEST) {
            final GameOverRequest gameOverRequest = (GameOverRequest) command;
            if (gameOverRequest.getSide() != null) {
                final Side gameOverInitiator = gameOverRequest.getSide();
                LOGGER.info("Сторона " + gameOverInitiator + " - сдалась.");
                client.playerResigned(gameOverInitiator); // Уведомляем сами себя о конце игры, а надо бы обоих клиентов
                client.gameOver(); // TODO: игра окончена
                ctx.channel().pipeline().remove(this);
                ctx.channel().pipeline().close(); // TODO: Пересоздаем игру (по хорошему должен быть хэндлер настройки игры перед стартом игры)
            }
            // Если все же null, то мы не знаем какая сторона вышла, кто победил
            // Ping должен предупредить, что определенный клиент пропал и тогда статус игры
            // с победой оставшегося игрока надо передать только одному (ну либо выйдет время на ход)

            // TODO: кто то должен инициировать конец игры в случае если игра закончилась по правилам
            // Нету GameInfo как понять что игра закончилась по правилам? После каждого MoveRequest проверять на конец игры
        }
    }
}
