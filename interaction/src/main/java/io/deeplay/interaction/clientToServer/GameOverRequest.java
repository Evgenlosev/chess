package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.core.model.Side;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

// Если клиент закончил игру раньше(например - сдался), надо уведомлять об этом
@JsonTypeName("GameOverRequest")
public class GameOverRequest extends Command {
    private final Side side;
    // Думаю сюда так же надо будет передавать GameStatus, если оба игрока согласятся на ничью,
    // однако запросов на ничью пока никаких нет. Без gameStatus, GameOverRequest означает, что игрок закрыл игру

    public GameOverRequest(final Side side) {
        this.side = side;
    }

    @JsonIgnore
    @Override
    public CommandType getCommandType() {
        return CommandType.GAME_OVER_REQUEST;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "GameOverRequest{" +
                "side=" + side +
                '}';
    }
}
