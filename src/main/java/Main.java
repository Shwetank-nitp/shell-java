import data.CommandContext;
import pipline.Pipeline;
import pipline.SimplePipeline;
import utils.ArgumentParser;
import utils.CommandResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Main application
    public static void main(String[] args) throws Exception {
        boolean isRunning = true;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(isRunning) {
            System.out.print("$ ");
            String str = br.readLine();

            try {
                String[] tokens = ArgumentParser.getArgs(str);
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
