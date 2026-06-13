import data.CommandContext;
import utils.ArgumentParser;
import utils.CommandResult;
import utils.Registry;
import writer.OutputManager;
import writer.OutputWriter;
import writer.SimpleOutputManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    private final static Registry registry = new Registry();
    private final static OutputManager outputManager = new SimpleOutputManager();

    public static void main(String[] args) throws Exception {
        boolean isRunning = true;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(isRunning) {
            System.out.print("$ ");

            String str = br.readLine();
            String[] tokens = ArgumentParser.getArgs(str);
            CommandContext context = CommandContext.getContext(tokens);

            try {
                OutputWriter writer = outputManager.getOutputWriter(context.getRedirectionOperator());

                CommandResult r = registry.execute(
                        writer, context
                );
                isRunning = r.REPLFlag(); // Check the Read-Evaluate-Print-Loop flag
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
