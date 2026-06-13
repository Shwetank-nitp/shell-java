package data;

import writer.OutputManager;

import java.util.ArrayList;
import java.util.List;

public class CommandContext {
    private final String commandName;
    private final String[] getArgs;
    private final String[] redirectionLocations;
    private final String redirectionOperator;

    private CommandContext(String commandName, String[] getArgs,
                          String[] redirectionLocations, String redirectionOperator) {
        this.commandName = commandName;
        this.getArgs = getArgs;
        this.redirectionLocations = redirectionLocations;
        this.redirectionOperator = redirectionOperator;
    }

    public static CommandContext getContext(String... args) {
        String commandName = args[0];

        // now next n till the operator is the args of the command
        List<String> commandArgs = new ArrayList<>();
        int n = args.length;
        int i = 1;

        while (i < n && !OutputManager.isOperator(args[i])) {
            commandArgs.add(args[i]);
            i++;
        }

        String oprt = null;
        if (i < n) {
            oprt = args[i];
        }
        List<String> locations = new ArrayList<>();

        while (++i < n) {
            locations.add(args[i]);
        }

        return new CommandContext(
                commandName,
                commandArgs.toArray(new String[0]),
                locations.toArray(new String[0]),
                oprt
        );
    }

    public String getCommandName() { return commandName; }
    public String[] getArgs() { return getArgs; }
    public String[] getRedirectionLocations() { return redirectionLocations; }
    public String getRedirectionOperator() { return redirectionOperator; }
    public boolean hasRedirection() { return redirectionOperator != null; }
}
