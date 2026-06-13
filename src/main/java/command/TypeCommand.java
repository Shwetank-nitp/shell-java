package command;

import data.CommandContext;
import error.InvalidCommand;
import utils.Executor;
import utils.Registry;

public class TypeCommand implements Command {
    CommandContext context;
    public TypeCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public String[] execute() throws InvalidCommand {
        String output;

        if (Registry.isBuiltin(context.getArgs()[0])) {
            output = context.getArgs()[0] + " is a shell builtin";
        } else output = Executor.getPath(context.getArgs()[0]);

        if (output == null) {
            output = context.getArgs()[0] + ": not found";
        }
        return new String[] {output, null, "yes"};
    }
}
