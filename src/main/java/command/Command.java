package command;

import data.ShellProcess;
import error.CommandRuntimeException;
import error.InvalidCommand;

public interface Command {
    ShellProcess execute() throws InvalidCommand, CommandRuntimeException;
}
