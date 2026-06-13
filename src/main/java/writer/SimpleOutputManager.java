package writer;

public class SimpleOutputManager extends OutputManager {
    @Override
    public OutputWriter getOutputWriter(String operator) {
        return switch (operator) {
            case OVERWRITE1, OVERWRITE2 -> new FileOverWriter();
            case APPEND1, APPEND2 -> new FileAppendWriter();
            default -> new SimpleConsoleWriter();
        };
    }

    @Override
    public OutputWriter getErrorWriter(String operator) {
        return switch (operator) {
            case OVERWRITE_ERROR -> new FileOverWriter();
            case APPEND_ERROR -> new FileAppendWriter();
            default -> new SimpleConsoleWriter();
        };
    }
}
