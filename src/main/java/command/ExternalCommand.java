package command;

import data.CommandContext;
import error.CommandRuntimeException;
import error.InvalidCommand;
import utils.Executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ExternalCommand implements Command{
    CommandContext context;

    public ExternalCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public String[] execute() throws InvalidCommand, CommandRuntimeException {
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

            String error = String.join("\n", errLines);

            return new String[] {output, error, "yes"};

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
