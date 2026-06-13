package utils;

import command.*;
import data.CommandContext;
import error.InvalidCommand;
import writer.OutputWriter;

import java.util.HashMap;

public class Registry {
    private final HashMap<String, Command> commandMap;
    private final ExternalCommand exeCommand;
    public Registry()  {
        commandMap = new HashMap<>();
        exeCommand = new ExternalCommand();

        commandMap.put("type", new TypeCommand(this));
        commandMap.put("exit", new ExitCommand());
        commandMap.put("echo", new EchoCommand());
    }

    public boolean isBuiltin(String command) {
        return commandMap.containsKey(command);
    }

    public CommandResult execute(OutputWriter writer, CommandContext context) throws InvalidCommand {
        if (isBuiltin(context.getCommandName())) return commandMap.get(context.getCommandName()).execute(writer, context);

        // if not a buildIn command
        return exeCommand.execute(writer, context);
    }
}
