package command;

import error.InvalidCommand;
import utils.CommandResult;

public interface Command {
    CommandResult execute(String... args) throws InvalidCommand;
}
