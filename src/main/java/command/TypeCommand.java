package command;

import data.CommandContext;
import error.InvalidCommand;
import utils.CommandResult;
import utils.Executor;
import utils.Registry;
import writer.OutputWriter;

public class TypeCommand implements Command {

    final Registry registry;
    public TypeCommand(Registry registry) {
        this.registry = registry;
    }

    @Override
    public CommandResult execute(OutputWriter writer, CommandContext context) throws InvalidCommand {
        if (registry.isBuiltin(context.getArgs()[0])) {
            System.out.println(context.getArgs()[0] + " is a shell builtin");
            return CommandResult.of(null);
        }
        String res = Executor.getPath(context.getArgs()[0]);

        if (res == null) throw new InvalidCommand(context.getArgs()[0] + ": not found"); // throw not found error
        writer.write(context.getArgs()[0] + " is " + res, context.getRedirectionLocations());
        return CommandResult.of(null);
    }
}
