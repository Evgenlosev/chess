package io.deeplay.interaction.serverToClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.CommandType;

@JsonTypeName("MoveResponse")
public class MoveResponse extends Command {
    private final boolean isMoveCorrect;
    //TODO Здесь передаем объект класса доски с текущем положением фигур
    @JsonCreator
    public MoveResponse(@JsonProperty("isMoveCorrect") final boolean isMoveCorrect) {
        super(CommandType.MOVE_RESPONSE);
        this.isMoveCorrect = isMoveCorrect;
    }

    public MoveResponse() {
        super(CommandType.MOVE_RESPONSE);
        this.isMoveCorrect = false;
    }
    @JsonProperty("isMoveCorrect")
    public boolean isMoveCorrect() {
        return isMoveCorrect;
    }

    @Override
    public String toString() {
        return "MoveResponse{" +
                "commandType='" + super.getCommandType() + '\'' +
                ", isMoveCorrect=" + isMoveCorrect +
                '}';
    }
}
