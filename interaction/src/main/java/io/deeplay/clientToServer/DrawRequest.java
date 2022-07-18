package io.deeplay.clientToServer;

import io.deeplay.Command;
import io.deeplay.CommandType;

public class DrawRequest extends Command {

    public DrawRequest() {
        super(CommandType.DRAW_REQUEST);
    }
}
