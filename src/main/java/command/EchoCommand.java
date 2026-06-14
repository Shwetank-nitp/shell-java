package command;

import data.CommandContext;
import error.InvalidCommand;

public class EchoCommand implements Command {
    CommandContext context;

    public EchoCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public String[] execute() throws InvalidCommand {
        StringBuilder echo = new StringBuilder();

        for(String s: context.getArgs()) {
            echo.append(s).append(" ");
        }

        echo.setLength(echo.length()-1);
        return new String[] {echo.toString(), null, "yes"};
    }
}
