package io.deeplay.server.session;

import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;
import io.deeplay.core.player.PlayerType;
import io.netty.channel.ChannelHandlerContext;

public class RemotePlayer extends Player {

    //Контекст общения клиента и сервера
    private final ChannelHandlerContext ctx;

    public RemotePlayer(final Side side, final ChannelHandlerContext ctx) {
        super(side);
        this.ctx = ctx;
    }

    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        throw new RuntimeException("RemotePlayer move has not been done yet");
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.REMOTE_PLAYER;
    }
}
