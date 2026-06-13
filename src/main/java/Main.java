import utils.ArgumentParser;
import utils.CommandResult;
import utils.Registry;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    private final static Registry registry = new Registry();

    public static void main(String[] args) throws Exception {
        boolean isRunning = true;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(isRunning) {
            System.out.print("$ ");

            String str = br.readLine();
            String[] tokens = ArgumentParser.getArgs(str);

            try {
                CommandResult r = registry.execute(tokens);
                isRunning = r.REPLFlag(); // Check the Read-Evaluate-Print-Loop flag
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
