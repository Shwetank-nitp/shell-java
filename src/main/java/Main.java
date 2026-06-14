import data.CommandContext;
import io.adaptor.IOAdapter;
import io.adaptor.JLineAdaptor;
import pipline.Pipeline;
import pipline.SimplePipeline;
import utils.ArgumentParser;
import utils.CommandResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

        while(isRunning) {
            String input = adapter.ioRead();
            try {
                String[] tokens = ArgumentParser.getArgs(input);
                CommandContext context = CommandContext.getContext(tokens);

                // make a list of exception
                List<CommandContext> contexts = new ArrayList<>();
                contexts.add(context);
                Pipeline pipeline = new SimplePipeline(contexts);

                CommandResult r = pipeline.run();
                isRunning = r.REPLFlag(); // Check the Read-Evaluate-Print-Loop flag
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
