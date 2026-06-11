import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    private static String searchExe(String program) {
        String path = System.getenv("PATH");
        String[] dirs = path.split(Pattern.quote(File.pathSeparator));

        for (String dir: dirs) {
            Path entity = Paths.get(dir, program);

            if (!Files.exists(entity))  continue;

            if(Files.isExecutable(entity)) {
                return entity.toString();
            }
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        // state variables and data
        boolean isRunning = true;
        final HashSet<String> hashSet = new HashSet<>();
        hashSet.add("type");
        hashSet.add("exit");
        hashSet.add("echo");

        while(isRunning) {
            System.out.print("$ ");
            Scanner sc = new Scanner(System.in);
    
            String[] tokens = sc.nextLine().split(" "); // break the command into tokens


            switch (tokens[0]) {
                case "exit" -> isRunning = false;
                case "echo" -> {
                    StringBuilder sb = new StringBuilder();
                    int i = 0;
                    int n = tokens.length - 1;

                    while (i++ < n) sb.append(tokens[i]).append(" ");

                    sb.deleteCharAt(sb.length() - 1);

                    String message = sb.toString();
                    System.out.println(message);
                }
                case "type" -> {
                    if (hashSet.contains(tokens[1])) System.out.println(tokens[1] + " is a shell builtin");
                    else {
                        String res = searchExe(tokens[1]);
                        System.out.println(Objects.requireNonNullElseGet(res, () -> tokens[1] + ": not found"));
                    }
                }
                default -> System.out.println(tokens[0] + ": command not found");
            }
        }
    }
}
