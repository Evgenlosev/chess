package io.deeplay.server.client;

import io.deeplay.core.listener.ChessListener;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import io.deeplay.core.player.Player;
import io.deeplay.core.player.PlayerType;
import io.deeplay.interaction.serverToClient.MoveResponse;
import io.deeplay.interaction.serverToClient.StartGameResponse;
import io.netty.channel.ChannelHandlerContext;


public class Client extends Player implements ChessListener {

    //Контекст общения клиента и сервера
    private ChannelHandlerContext ctx;
    private final Object monitor;
    private MoveInfo currentMove;

    public Client(final Side side, final ChannelHandlerContext ctx) {
        super(side);
        this.ctx = ctx;
        monitor = new Object();
    }

    public Client(final Side side) {
        super(side);
        monitor = new Object();
    }

    public void setCtx(final ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void setCurrentMove(final MoveInfo currentMove) {
        this.currentMove = currentMove;
        monitor.notifyAll();
    }

    public Object getMonitor() {
        return monitor;
    }

    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return currentMove;
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.CLIENT;
    }

    @Override
    public void gameStarted() {
        ctx.writeAndFlush(new StartGameResponse(true));
    }

    @Override
    public void playerSeated(final Side side) {

    }

    @Override
    public void playerActed(final Side side, final MoveInfo moveInfo) {
        ctx.writeAndFlush(new MoveResponse(moveInfo, side));
    }

    @Override
    public void offerDraw(final Side side) {

    }

    @Override
    public void acceptDraw(final Side side) {

    }

    @Override
    public void playerRequestsTakeBack(final Side side) {

    }

    @Override
    public void playerAgreesTakeBack(final Side side) {

    }

    @Override
    public void playerResigned(final Side side) {

    }

    @Override
    public void draw() {

    }

    @Override
    public void playerWon(final Side side) {

    }

    @Override
    public void gameOver() {

    }

}
