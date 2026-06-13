package command;

import data.CommandContext;
import error.InvalidCommand;
import utils.CommandResult;
import writer.OutputWriter;

public class ExitCommand implements Command{
    @Override
    public CommandResult execute(OutputWriter writer, CommandContext context) throws InvalidCommand {
        return CommandResult.exit();
    }
}
