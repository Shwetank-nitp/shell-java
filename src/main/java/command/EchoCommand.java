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
        String echo = String.join(" ", context.getArgs()).trim();
        return new String[] {echo, null, "yes"};
    }
}
