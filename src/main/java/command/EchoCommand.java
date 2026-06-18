package command;

import data.CommandContext;
import data.ShellProcess;
import error.InvalidCommand;

public class EchoCommand implements Command {
    CommandContext context;

    public EchoCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public ShellProcess execute() throws InvalidCommand {
        StringBuilder echo = new StringBuilder();

        for(String s: context.getArgs()) {
            echo.append(s).append(" ");
        }

        echo.setLength(echo.length()-1);
        ShellProcess p = new ShellProcess(context, ProcessHandle.current().pid(), true, true);
        p.setOutput(echo.toString());

        return p;
    }
}
