import data.CommandContext;
import io.adaptor.IOAdapter;
import io.adaptor.JLineAdaptor;
import process.manager.ProcessManager;
import process.manager.SimpleProcessManager;
import utils.ArgumentParser;
import utils.CommandResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static final IOAdapter adapter;

    static {
        try {
            adapter = new JLineAdaptor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Main application
    public static void main(String[] args) throws Exception {
        boolean isRunning = true;
        ProcessManager processManager = SimpleProcessManager.getInstance();

        while(isRunning) {
            String input = adapter.ioRead();
            try {
                String[] tokens = ArgumentParser.getArgs(input);
                CommandContext context = CommandContext.getContext(tokens);

                // make a list of exception
                List<CommandContext> contexts = new ArrayList<>();
                contexts.add(context);

                List<CommandResult> r = processManager.run(contexts);
                for (CommandResult result: r) {
                    isRunning = result.REPLFlag() && isRunning; // Check the Read-Evaluate-Print-Loop flag
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
