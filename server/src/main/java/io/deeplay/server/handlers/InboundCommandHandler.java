package io.deeplay.server.handlers;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.clientToServer.MoveRequest;
import io.deeplay.server.client.Client;
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
        switch (command.getCommandType()) {
            case MOVE_REQUEST:
                MoveRequest moveRequest = (MoveRequest) command;
                MoveInfo moveInfo = moveRequest.getMoveInfo();
                synchronized (client.getMonitor()) {
                    client.setCurrentMove(moveInfo);
                }
                break;
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOGGER.error("Соединение с клиентом прервано", cause);
        ctx.close();
    }
}
