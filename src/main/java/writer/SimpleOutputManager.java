package writer;

public class SimpleOutputManager extends OutputManager {
    @Override
    public OutputWriter getOutputWriter(String operator) {
        if (tokenMap.containsKey(operator))
            return outStrategy.get(tokenMap.get(operator));

        // send the console operator by default
        return outStrategy.get(OUTPUT_PIPELINE.CONSOLE);
    }
}
