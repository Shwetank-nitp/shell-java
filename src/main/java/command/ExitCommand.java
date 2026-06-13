package command;

import data.CommandContext;
import error.InvalidCommand;
import utils.CommandResult;
import writer.OutputWriter;

public class ExitCommand implements Command{
    CommandContext context;

    public ExitCommand(CommandContext context) {
        this.context = context;
    }
    @Override
    public String[] execute() throws InvalidCommand {
        return new String[] {null, null, "no"};
    }
}
