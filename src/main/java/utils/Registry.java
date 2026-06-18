package utils;

import command.*;
import data.CommandContext;
import error.InvalidCommand;

import java.util.HashSet;

public class Registry {
    private static final HashSet<String> builtin;
    static {
        builtin = new HashSet<>();

        builtin.add("type");
        builtin.add("exit");
        builtin.add("echo");
        builtin.add("jobs");
    }

    public static boolean isBuiltin(String command) {
        return builtin.contains(command);
    }

    public static String[] execute(CommandContext context) throws InvalidCommand {
        return switch (context.getCommandName()) {
            case "type" -> new TypeCommand(context).execute();
            case "exit" -> new ExitCommand(context).execute();
            case "echo" -> new EchoCommand(context).execute();
            case "jobs" -> new JobsCommand(context).execute();
            default -> new ExternalCommand(context).execute();
        };
    }

    public static String[] getBuiltin() {
        return builtin.toArray(new String[0]);
    }
}
