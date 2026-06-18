package command;

import data.CommandContext;
import data.ShellProcess;
import error.CommandRuntimeException;
import error.InvalidCommand;
import process.manager.SimpleProcessManager;

import java.util.List;

public class JobsCommand implements Command {
    private final CommandContext context;

    public JobsCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public ShellProcess execute() throws InvalidCommand, CommandRuntimeException {
        List<ShellProcess> runningProcess = SimpleProcessManager
                .getInstance()
                .getRunningProcess();
        // do the rest later

        return new ShellProcess(context, ProcessHandle.current().pid(), true, true);
    }
}
