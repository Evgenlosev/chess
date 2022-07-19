package io.deeplay.interaction;

public abstract class Command {
    private final CommandType commandType;

    public Command(final CommandType commandType) {
        this.commandType = commandType;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
