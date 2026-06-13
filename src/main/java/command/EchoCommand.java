package command;

import error.InvalidCommand;
import utils.CommandResult;

import java.util.Arrays;

public class EchoCommand implements Command{

    @Override
    public CommandResult execute(String... args) throws InvalidCommand {

        String echo = String.join(
                " ",
                Arrays.copyOfRange(args, 1, args.length)
        ).trim();

        System.out.println(echo);
        return CommandResult.of(null);
    }
}
