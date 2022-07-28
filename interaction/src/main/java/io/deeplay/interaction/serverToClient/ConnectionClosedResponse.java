package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("ConnectionClosedResponse")
public class ConnectionClosedResponse extends Command {
    private String errorMessage;

    public ConnectionClosedResponse() {
        super(CommandType.CONNECTION_CLOSED_RESPONSE);
    }

    public ConnectionClosedResponse(final String errorMessage) {
        super(CommandType.CONNECTION_CLOSED_RESPONSE);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ConnectionClosedResponse{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
