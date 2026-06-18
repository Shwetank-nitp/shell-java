package command;

import data.CommandContext;
import data.ShellProcess;
import error.InvalidCommand;

public class ExitCommand implements Command{
    CommandContext context;

    public ExitCommand(CommandContext context) {
        this.context = context;
    }
    @Override
    public ShellProcess execute() throws InvalidCommand {
        return new ShellProcess(context, ProcessHandle.current().pid(), true, false);
    }
}
