package command;

import error.InvalidCommand;
import utils.CommandResult;
import utils.Executor;

import java.io.IOException;

public class ExternalCommand implements Command{
    @Override
    public CommandResult execute(String... args) throws InvalidCommand {
        if (!Executor.isExecutable(args[0])) {
            throw InvalidCommand.notFound(args[0]);
        }

        // write the logic to execute the file
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.inheritIO(); // join the oi to my shell

        try {
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new CommandResult("success", true);
    }
}
