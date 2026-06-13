package command;

import data.CommandContext;
import error.CommandRuntimeException;
import error.InvalidCommand;
import utils.CommandResult;
import utils.Executor;
import writer.OutputWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ExternalCommand implements Command{
    @Override
    public CommandResult execute(OutputWriter writer, CommandContext context) throws InvalidCommand, CommandRuntimeException {
        if (!Executor.isExecutable(context.getCommandName())) {
            throw InvalidCommand.notFound(context.getCommandName());
        }

        // write the logic to execute the file
        String[] fullCommand = Stream.concat(
                Stream.of(context.getCommandName()),
                Arrays.stream(context.getArgs())
        ).toArray(String[]::new);

        ProcessBuilder pb = new ProcessBuilder(fullCommand);
        pb.redirectErrorStream(false);

        try {
            Process p = pb.start();

            BufferedReader stdout =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stderr =
                    new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String line;
            List<String> outLines = new ArrayList<>();
            while ((line = stdout.readLine()) != null) outLines.add(line);

            List<String> errLines = new ArrayList<>();
            while ((line = stderr.readLine()) != null) errLines.add(line);

            p.waitFor();

            String output = String.join("\n", outLines);
            writer.write(output, context.getRedirectionLocations());

            String error = String.join("\n", errLines);
            if (!error.isEmpty()) {
                throw new CommandRuntimeException(error);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new CommandResult("success", true);
    }
}
