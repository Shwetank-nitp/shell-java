package utils;

import command.*;
import error.InvalidCommand;

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

    public CommandResult execute(String... args) throws InvalidCommand {
        if (isBuiltin(args[0])) return commandMap.get(args[0]).execute(args);
        // if not a buildIn command
        return exeCommand.execute(args);
    }
}
