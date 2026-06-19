package command;

import data.CommandContext;
import data.ShellProcess;
import error.CommandRuntimeException;
import error.InvalidCommand;
import process.manager.ProcessManager;
import process.manager.SimpleProcessManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobsCommand implements Command {
    private final CommandContext context;

    public JobsCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public ShellProcess execute() throws InvalidCommand, CommandRuntimeException {
        List<ShellProcess> jobs = SimpleProcessManager.getInstance().getRunningProcess();

        for (int i = 0; i < jobs.size(); i++) {
            ShellProcess p = jobs.get(i);

            String marker;
            if (i == 0) {
                marker = "+";
            } else if (i == 1) {
                marker = "-";
            } else {
                marker = "";
            }

            String command = p.getContext().getCommandName();
            if (p.getContext().getArgs().length > 0) {
                command += " " + String.join(" ", p.getContext().getArgs());
            }

            String line = "[%d]%s  Running                 %s"
                    .formatted(i + 1, marker, command);

            System.out.println(line);
        }

        return new ShellProcess(context, ProcessHandle.current().pid(), true, true);
    }
}
