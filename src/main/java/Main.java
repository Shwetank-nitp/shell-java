import java.util.Scanner;

public class Main {
    private static void printCommandNotFoundErrorMessage(String command) {
        String error = command+": command not found";
        System.out.println(error);
    }

    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
        
        boolean isRunning = true;
        
        while(isRunning) {
            System.out.print("$ ");
            Scanner sc = new Scanner(System.in);
    
            String command = sc.nextLine();
            if (command.equals("exit")) isRunning = false;
            // print the error message
            else printCommandNotFoundErrorMessage(command);
        }
    }
}
