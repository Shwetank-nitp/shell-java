package command;

import error.InvalidCommand;
import utils.CommandResult;

public class EchoCommand implements Command{

    @Override
    public CommandResult execute(String... args) throws InvalidCommand {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int n = args.length - 1;

        while (i++ < n) sb.append(args[i]).append(" ");

        sb.deleteCharAt(sb.length() - 1);

        String message = sb.toString();
        System.out.println(message);

        return CommandResult.of(null);
    }
}
