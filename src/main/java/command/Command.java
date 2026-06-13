package command;

import error.CommandRuntimeException;
import error.InvalidCommand;

public interface Command {
    String[] execute() throws InvalidCommand, CommandRuntimeException;
}
