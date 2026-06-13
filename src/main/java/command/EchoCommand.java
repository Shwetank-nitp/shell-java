package command;

import data.CommandContext;
import error.InvalidCommand;
import utils.CommandResult;
import writer.OutputWriter;

import java.util.ArrayList;
import java.util.List;

public class EchoCommand implements Command {

    @Override
    public CommandResult execute(OutputWriter writer, CommandContext context) throws InvalidCommand {
        String echo = String.join(" ", context.getArgs()).trim();
        
        writer.write(echo, context.getRedirectionLocations());
        return CommandResult.of(null);
    }
}
