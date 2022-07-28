package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("Ping")
public class Ping extends Command {

    public Ping() {
        super(CommandType.PING);
    }

    @Override
    public String toString() {
        return "Ping{" +
                "commandType='" + super.getCommandType() + '\'' +
                "}";
    }
}
