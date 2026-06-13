package command;

import data.CommandContext;
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
    public CommandResult execute(OutputWriter writer, CommandContext context) throws InvalidCommand {
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
            List<String> lines = new ArrayList<>();
            while ((line = stdout.readLine()) != null) lines.add(line);

            while ((line = stderr.readLine()) != null) System.out.println(line);

            p.waitFor();

            String output = String.join("\n", lines);
            writer.write(output, context.getRedirectionLocations());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new CommandResult("success", true);
    }
}
