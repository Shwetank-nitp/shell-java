package utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Executor {
    public static boolean isExecutable(String program) {
        String pathVar = System.getenv("PATH");

        String[] dirs = pathVar.split(Pattern.quote(File.pathSeparator));

        for(String d: dirs) {
            Path f = Paths.get(d, program);
            if (!Files.exists(f)) continue;

            // check the executable permissions
            if (Files.isExecutable(f)) return true;
        }

        return false;
    }

    public static String getPath(String program) {
        String pathVar = System.getenv("PATH");

        String[] dirs = pathVar.split(Pattern.quote(File.pathSeparator));

        for(String d: dirs) {
            Path f = Paths.get(d, program);
            if (!Files.exists(f)) continue;

            // check the executable permissions
            if (Files.isExecutable(f)) return f.toString();
        }

        return null;
    }
}
