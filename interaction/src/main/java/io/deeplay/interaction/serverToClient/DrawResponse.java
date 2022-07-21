package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("DrawResponse")
public class DrawResponse extends Command {
    private final boolean isDrawAgreed;

    @JsonCreator()
    public DrawResponse(@JsonProperty("isDrawAgreed") final boolean isDrawAgreed) {
        super(CommandType.DRAW_RESPONSE);
        this.isDrawAgreed = isDrawAgreed;
    }

    public DrawResponse() {
        super(CommandType.DRAW_RESPONSE);
        this.isDrawAgreed = false;
    }

    @JsonProperty("isDrawAgreed")
    public boolean isDrawAgreed() {
        return isDrawAgreed;
    }

    @Override
    public String toString() {
        return "DrawResponse{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", isDrawAgreed=" + isDrawAgreed +
                '}';
    }
}
