package writer;

import java.util.Set;

public abstract class OutputManager {
    public abstract OutputWriter getOutputWriter(String operator);
    public abstract OutputWriter getErrorWriter(String operator);

    private static final Set<String> REDIRECTION_TOKENS = Set.of(">", "1>", "2>", ">>", "1>>", "2>>");
    protected static final String APPEND1 = ">>";
    protected static final String APPEND2 = "1>>";
    protected static final String APPEND_ERROR = "2>>";

    protected static final String OVERWRITE1 = ">";
    protected static final String OVERWRITE2 = "1>";
    protected static final String OVERWRITE_ERROR = "2>";

    public static boolean isOperator(String x) {
        return REDIRECTION_TOKENS.contains(x);
    }

}
