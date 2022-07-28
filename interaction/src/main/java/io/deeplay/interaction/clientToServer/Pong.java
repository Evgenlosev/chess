package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("Pong")
public class Pong extends Command {

    public Pong() {
        super(CommandType.PONG);
    }

    @Override
    public String toString() {
        return "Pong{" +
                "commandType='" + super.getCommandType() + '\'' +
                "}";
    }
}
