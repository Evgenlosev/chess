package io.deeplay.client.session;

import ch.qos.logback.classic.Logger;
import io.deeplay.client.gui.Gui;
import io.deeplay.core.console.BoardDrawer;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.player.Player;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.clientToServer.MoveRequest;
import io.deeplay.interaction.serverToClient.GameOverResponse;
import io.deeplay.interaction.serverToClient.MoveResponse;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.LoggerFactory;
import java.util.function.Consumer;


public class ClientGameSession {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ClientGameSession.class);
    private final Player player;

    private final GameInfo gameInfo;

    private final Gui gui;

    private final ChannelHandlerContext ctx;

    Consumer<MoveInfo> function;

    public ClientGameSession(final Player player, final ChannelHandlerContext ctx) {
        this.player = player;
        this.ctx = ctx;
        this.gameInfo = new GameInfo();
        function = x -> ctx.writeAndFlush(new MoveRequest(x));
        gui = new Gui(function);
        gui.updateBoard(gameInfo.getFenBoard(), gameInfo.getAvailableMoves());
        gui.setVisible(true);
    }

    public void start() {
        BoardDrawer.draw(gameInfo.getFenBoard());
    }

    public void acceptCommand(Command command) {
        switch (command.getCommandType()) {
            case GET_ANSWER:
                makeMove();
                break;
            case MOVE_RESPONSE:
                MoveResponse moveResponse = (MoveResponse) command;
                updateBoard(moveResponse.getMoveInfo());
                break;
            case GAME_OVER_RESPONSE:
                GameOverResponse gameOverResponse = (GameOverResponse) command;
                LOGGER.info("Игра завершена: {}", gameOverResponse.getGameStatus().getMessage());
                break;
        }
    }

    private void updateBoard(MoveInfo moveInfo) {
        gameInfo.updateBoard(moveInfo);
        gui.updateBoard(gameInfo.getFenBoard(), gameInfo.getAvailableMoves());
    }


    private void makeMove() {
    }
}
