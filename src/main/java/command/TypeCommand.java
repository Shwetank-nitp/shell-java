package command;

import error.InvalidCommand;
import utils.CommandResult;
import utils.Executor;
import utils.Registry;

public class TypeCommand implements Command {

    final Registry registry;
    public TypeCommand(Registry registry) {
        this.registry = registry;
    }

    @Override
    public CommandResult execute(String... args) throws InvalidCommand {
        if (registry.isBuiltin(args[1])) {
            System.out.println(args[1] + " is a shell builtin");
            return CommandResult.of(null);
        }
        String res = Executor.getPath(args[1]);

        if (res == null) throw new InvalidCommand(args[1]+ ": not found"); // throw not found error
        System.out.println(args[1] + " is " + res);

        return CommandResult.of(null);
    }
}
