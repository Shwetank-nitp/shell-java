package writer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class OutputManager {
    public abstract OutputWriter getOutputWriter(String operator);

    private static final Set<String> REDIRECTION_TOKENS = Set.of(">", "1>", "2>", ">>", "1>>", "2>>");
    protected final static Map<OutputManager.OUTPUT_PIPELINE, OutputWriter> outStrategy;
    protected final static Map<String, OutputManager.OUTPUT_PIPELINE> tokenMap;

    static {
        outStrategy = new HashMap<>();
        outStrategy.put(OutputManager.OUTPUT_PIPELINE.FILE_APPEND, new FileAppendWriter());
        outStrategy.put(OutputManager.OUTPUT_PIPELINE.FILE_OVERWRITE, new FileOverWriter());
        outStrategy.put(OutputManager.OUTPUT_PIPELINE.CONSOLE, new SimpleConsoleWriter());

        tokenMap = new HashMap<>();

        // Overwrite tokens
        tokenMap.put(">", OutputManager.OUTPUT_PIPELINE.FILE_OVERWRITE);
        tokenMap.put("1>", OutputManager.OUTPUT_PIPELINE.FILE_OVERWRITE);
        tokenMap.put("2>", OutputManager.OUTPUT_PIPELINE.FILE_OVERWRITE);

        // Append tokens
        tokenMap.put(">>", OutputManager.OUTPUT_PIPELINE.FILE_APPEND);
        tokenMap.put("1>>", OutputManager.OUTPUT_PIPELINE.FILE_APPEND);
        tokenMap.put("2>>", OutputManager.OUTPUT_PIPELINE.FILE_APPEND);
    }

    // Static interface method (Available everywhere as OutputManager.isOperator("..."))
    public static boolean isOperator(String x) {
        return REDIRECTION_TOKENS.contains(x);
    }

    public enum OUTPUT_PIPELINE {
        FILE_APPEND,
        FILE_OVERWRITE,
        CONSOLE
    }
}
