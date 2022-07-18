package io.deeplay.interaction.serverToClient;

import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

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
