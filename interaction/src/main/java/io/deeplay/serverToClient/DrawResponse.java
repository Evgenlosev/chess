package io.deeplay.serverToClient;

import io.deeplay.Command;
import io.deeplay.CommandType;

public class DrawResponse extends Command {
    private final boolean isDrawAgreed;

    public DrawResponse(final boolean isDrawAgreed) {
        super(CommandType.DRAW_RESPONSE);
        this.isDrawAgreed = isDrawAgreed;
    }

    public boolean isDrawAgreed() {
        return isDrawAgreed;
    }
}
