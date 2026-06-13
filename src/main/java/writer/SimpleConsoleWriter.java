package writer;

public class SimpleConsoleWriter implements OutputWriter {

    @Override
    public void write(String output, String... fileLocations) {
        if (output == null || output.isEmpty()) return;
        System.out.println(output);
    }
}
