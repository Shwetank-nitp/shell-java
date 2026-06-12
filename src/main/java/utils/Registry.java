package utils;

import command.Command;
import command.EchoCommand;
import command.ExitCommand;
import command.TypeCommand;
import error.InvalidCommand;

import java.io.IOException;
import java.util.HashMap;

public class Registry {
    private static final HashMap<String, Command> commandMap;
    static  {
        commandMap = new HashMap<>();

        commandMap.put("type", new TypeCommand());
        commandMap.put("exit", new ExitCommand());
        commandMap.put("echo", new EchoCommand());
    }

    public static boolean isBuiltin(String command) {
        return commandMap.containsKey(command);
    }

    public CommandResult execute(String... args) throws InvalidCommand, IOException, InterruptedException {
        if (isBuiltin(args[0])) return commandMap.get(args[0]).execute(args);
        if (!Executor.isExecutable(args[0])) {
            throw InvalidCommand.notFound(args[0]);
        }

        // write the logic to execute the file
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.inheritIO(); // join the oi to my shell

        Process p = pb.start();
        p.waitFor();

        return new CommandResult("success", true);
    }
}
