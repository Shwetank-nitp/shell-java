package process.manager;

import data.CommandContext;
import data.ShellProcess;
import utils.CommandResult;
import utils.Registry;
import writer.OutputManager;
import writer.OutputWriter;
import writer.SimpleOutputManager;

import java.util.ArrayList;
import java.util.List;

public class SimpleProcessManager implements ProcessManager {
    private static final SimpleProcessManager INSTANCE =
            new SimpleProcessManager();

    private final OutputManager manager;
    private final List<ShellProcess> shellProcesses;

    private SimpleProcessManager() {
        this.shellProcesses = new ArrayList<>();
        this.manager = new SimpleOutputManager();
    }

    public static SimpleProcessManager getInstance() {
        return INSTANCE;
    }

    @Override
    public List<CommandResult> run(List<CommandContext> contexts) {
        List<ShellProcess> responses = new ArrayList<>();

        for (CommandContext context: contexts) {
            ShellProcess response = Registry.execute(context);
            responses.add(response);
        }

        // process the response
        int n = contexts.size();
        List<CommandResult> results = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            CommandContext context = contexts.get(i);
            ShellProcess r = responses.get(i);

            if (!r.isCompleted()) {
                this.shellProcesses.add(r);
            }

            OutputWriter outputWriter = manager.getOutputWriter(
                    context.getRedirectionOperator()
            );

            OutputWriter errorWriter = manager.getErrorWriter(
                    context.getRedirectionOperator()
            );

            outputWriter.write(r.getOutput(), context.getRedirectionLocations());
            errorWriter.write(r.getError(), context.getRedirectionLocations());

            if (!r.isREPLflag()) {
                results.add(CommandResult.exit());
            } else {
                results.add(CommandResult.of(null));
            }
        }

        return results;
    }

    @Override
    public List<ShellProcess> getRunningProcess() {
        return shellProcesses;
    }

}
