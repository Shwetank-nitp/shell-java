package command;

import error.InvalidCommand;
import utils.CommandResult;

public class ExitCommand implements Command{
    @Override
    public CommandResult execute(String... args) throws InvalidCommand {
        return CommandResult.exit();
    }
}
