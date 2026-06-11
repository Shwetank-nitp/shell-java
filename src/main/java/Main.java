import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static void printCommandNotFoundErrorMessage(String command) {
        String error = command+": command not found";
        System.out.println(error);
    }

    private static HashMap<String, String> map = new HashMap<>();

    private static void handleType(String type) {
        if (map.containsKey(type)) {
            System.out.println(type+" "+map.get(type));
        } else {
            System.out.println(type+" not found");
        }
    }

    public static void main(String[] args) throws Exception {
        map = new HashMap<>();
        map.put("exit", "is a shell builtin");
        map.put("echo", "is a shell builtin");
        map.put("type", "is a shell builtin");

        boolean isRunning = true;
        
        while(isRunning) {
            System.out.print("$ ");
            Scanner sc = new Scanner(System.in);
    
            String[] tokens = sc.nextLine().split(" "); // break the command into tokens


            if (tokens[0].equals("exit")) isRunning = false;
            else if (tokens[0].equals("echo")) {
                StringBuilder sb = new StringBuilder();
                int i = 0;
                int n = tokens.length-1;

                while (i++ < n) sb.append(tokens[i]).append(" ");

                sb.deleteCharAt(sb.length()-1);

                String message = sb.toString();
                System.out.println(message);
            } else if (tokens[0].equals("type")) {
                handleType(tokens[1]);
            }
            // print the error message
            else printCommandNotFoundErrorMessage(tokens[0]);
        }
    }
}
