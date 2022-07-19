package io.deeplay.interaction.clientToServer;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

public class DrawRequest extends Command {

    public DrawRequest() {
        super(CommandType.DRAW_REQUEST);
    }
}
