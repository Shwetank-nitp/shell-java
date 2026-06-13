package writer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileAppendWriter implements OutputWriter {
    @Override
    public void write(String output, String... fileLocations) {
        if (output == null) {
            output = "";
        }
        try {
            for (String fileLoc: fileLocations) {
                Path p = Paths.get(fileLoc);

                Files.writeString(
                        p,
                        output.isEmpty() ? output : output+"\n",
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                );
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to write to file: " + ex.getMessage(), ex);
        }
    }
}
