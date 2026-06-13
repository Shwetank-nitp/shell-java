package command;

import data.CommandContext;
import error.InvalidCommand;
import utils.CommandResult;
import writer.OutputWriter;

public interface Command {
    CommandResult execute(OutputWriter writer, CommandContext context) throws InvalidCommand;
}
