package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("DrawResponse")
public class DrawResponse extends Command {
    private final boolean drawAgreed;

    public DrawResponse(final boolean drawAgreed) {
        super(CommandType.DRAW_RESPONSE);
        this.drawAgreed = drawAgreed;
    }

    public DrawResponse() {
        super(CommandType.DRAW_RESPONSE);
        this.drawAgreed = false;
    }

    public boolean isDrawAgreed() {
        return drawAgreed;
    }

    @Override
    public String toString() {
        return "DrawResponse{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", drawAgreed=" + drawAgreed +
                '}';
    }
}
