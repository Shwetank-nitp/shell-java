package io.adaptor;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JLineAdaptor implements IOAdapter {
    private boolean tabFlag = false;
    private String lastBuffer = null;
    private final LineReader reader;

    public JLineAdaptor() throws IOException {
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .encoding(StandardCharsets.UTF_8)
                .build();

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(null);

        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(parser)
                .build();


        Completer completer = new ShellCompleter();

        final Widget tabAutoCompletion = () -> {
            String currBuffer = reader.getBuffer().toString();
            ParsedLine parsedLine =
                    parser.parse(currBuffer, currBuffer.length());

            List<Candidate> candidates = new ArrayList<>();
            completer.complete(reader, parsedLine, candidates);

            if (!parsedLine.word().equals(lastBuffer)) {
                tabFlag = false;
            }

            if (!tabFlag) {
                if (candidates.size() != 1) {
                    tabFlag = true;

                    terminal.writer().write('\u0007');
                    terminal.writer().flush();
                } else {
                    tabFlag = false;
                    // auto-complete
                    String candidate = candidates.getFirst().value();

                    String suffix = candidate.substring(parsedLine.word().length());
                    reader.getBuffer().write(suffix + " ");
                }
            } else {
                tabFlag = false;
                if (candidates.isEmpty()) return true;

                terminal.writer().println();

                for (Candidate c : candidates) {
                    terminal.writer().print(c.value());
                    terminal.writer().print("  ");
                }

                terminal.writer().println();
                terminal.writer().flush();

                reader.callWidget(LineReader.REDRAW_LINE);
                reader.callWidget(LineReader.REDISPLAY);
            }
            return true;
        };

        reader.getWidgets().put("shell-tab", tabAutoCompletion);

        reader.getKeyMaps()
                .get(LineReader.MAIN)
                .bind(new Reference("shell-tab"), "\t");
    }



    @Override
    public String ioRead() {
        return reader.readLine("$ ");
    }
}