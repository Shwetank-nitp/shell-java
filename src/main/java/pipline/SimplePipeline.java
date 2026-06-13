package pipline;

import data.CommandContext;
import utils.CommandResult;
import utils.Registry;
import writer.OutputManager;
import writer.OutputWriter;
import writer.SimpleOutputManager;

import java.util.ArrayList;
import java.util.List;

public class SimplePipeline implements Pipeline {
    private final List<CommandContext> contexts;
    private final OutputManager manager;

    public SimplePipeline (List<CommandContext> contexts) {
        this.contexts = contexts;
        this.manager = new SimpleOutputManager();
    }

    @Override
    public CommandResult run() {
        List<String[]> responses = new ArrayList<>();

        for (CommandContext context: contexts) {
            String[] response = Registry.execute(context);
            responses.add(response);
        }

        // process the response
        boolean flag = true;
        int n = contexts.size();

        for (int i = 0; i < n; i++) {
            CommandContext context = contexts.get(i);
            String[] r = responses.get(i);

            OutputWriter outputWriter = manager.getOutputWriter(
                    context.getRedirectionOperator()
            );

            OutputWriter errorWriter = manager.getErrorWriter(
                    context.getRedirectionOperator()
            );

            outputWriter.write(r[0], context.getRedirectionLocations());
            errorWriter.write(r[1], context.getRedirectionLocations());

            flag = flag && r[2].equals("yes");
        }

        if (!flag) return CommandResult.exit();

        return CommandResult.of(null);
    }

}
