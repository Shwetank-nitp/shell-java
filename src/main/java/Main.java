import java.util.HashSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        boolean isRunning = true;
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("type");
        hashSet.add("exit");
        hashSet.add("echo");

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
                if (hashSet.contains(tokens[1])) System.out.println(tokens[1]+ " is a shell builtin");
                else System.out.println(tokens[1] + " not found");
            }
            else System.out.println(tokens[0]+": command not found");
        }
    }
}
