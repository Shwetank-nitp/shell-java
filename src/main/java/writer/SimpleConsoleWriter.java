package writer;

public class SimpleConsoleWriter implements OutputWriter {

    @Override
    public void write(String output, String... fileLocations) {
        System.out.println(output);
    }
}
