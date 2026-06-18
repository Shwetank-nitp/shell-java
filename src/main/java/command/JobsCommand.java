package command;

import data.CommandContext;
import error.CommandRuntimeException;
import error.InvalidCommand;

public class JobsCommand implements Command {
    private final CommandContext context;

    public JobsCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public String[] execute() throws InvalidCommand, CommandRuntimeException {
        return new String[]{null, null, "yes"};
    }
}
