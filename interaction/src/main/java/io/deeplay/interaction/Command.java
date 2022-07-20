package io.deeplay.interaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.deeplay.interaction.clientToServer.*;
import io.deeplay.interaction.serverToClient.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthRequest.class, name = "AuthRequest"),
        @JsonSubTypes.Type(value = CancelMoveRequest.class, name = "CancelMoveRequest"),
        @JsonSubTypes.Type(value = DrawRequest.class, name = "DrawRequest"),
        @JsonSubTypes.Type(value = GameOverRequest.class, name = "GameOverRequest"),
        @JsonSubTypes.Type(value = MoveRequest.class, name = "MoveRequest"),
        @JsonSubTypes.Type(value = ProtocolVersionRequest.class, name = "ProtocolVersionRequest"),
        @JsonSubTypes.Type(value = StartGameRequest.class, name = "StartGameRequest"),
        @JsonSubTypes.Type(value = CancelMoveResponse.class, name = "CancelMoveResponse"),
        @JsonSubTypes.Type(value = AuthResponse.class, name = "AuthResponse"),
        @JsonSubTypes.Type(value = DrawResponse.class, name = "DrawResponse"),
        @JsonSubTypes.Type(value = GameOverResponse.class, name = "GameOverResponse"),
        @JsonSubTypes.Type(value = MoveResponse.class, name = "MoveResponse"),
        @JsonSubTypes.Type(value = ProtocolVersionResponse.class, name = "ProtocolVersionResponse"),
        @JsonSubTypes.Type(value = StartGameResponse.class, name = "StartGameResponse")
})
public abstract class Command {
    private final CommandType commandType;

    public Command(final CommandType commandType) {
        this.commandType = commandType;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
