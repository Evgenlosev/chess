package io.deeplay.interaction.clientToServer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("DrawRequest")
public class DrawRequest extends Command {

    public DrawRequest() {
        super(CommandType.DRAW_REQUEST);
    }

    @Override
    public String toString() {
        return "DrawRequest{" +
                "commandType='" + super.getCommandType() + '\'' +
                "}";
    }
}
