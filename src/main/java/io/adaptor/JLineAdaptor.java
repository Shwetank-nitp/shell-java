package io.adaptor;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JLineAdaptor implements IOAdapter {
    private boolean tabFlag = false;
    private String lastLookupWord = ""; // Tracks changes between keypresses
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
            Buffer buffer = reader.getBuffer();
            String currBuffer = buffer.toString();
            int cursor = buffer.cursor();

            // Parse context-aware using the exact cursor position
            ParsedLine parsedLine = parser.parse(currBuffer, cursor, Parser.ParseContext.COMPLETE);
            String currentWord = parsedLine.word();

            List<Candidate> candidates = new ArrayList<>();
            completer.complete(reader, parsedLine, candidates);

            Collections.sort(candidates);
            // Guard clause: If nothing matches, beep and reset state immediately
            if (candidates.isEmpty()) {
                tabFlag = false;
                lastLookupWord = currentWord;
                terminal.writer().write('\u0007');
                terminal.flush();
                return true;
            }

            // If the user modified the text since the last tab, reset the state machine
            if (!currentWord.equals(lastLookupWord)) {
                tabFlag = false;
            }
            lastLookupWord = currentWord;

            if (!tabFlag) {
                if (candidates.size() == 1) {
                    // Perfect match: autocomplete right away
                    String candidate = candidates.getFirst().value();
                    String suffix = candidate.substring(currentWord.length());
                    buffer.write(suffix + " ");
                    tabFlag = false;
                } else {
                    // Ambiguous match: beep on first tab, wait for second tab
                    tabFlag = true;
                    terminal.writer().write('\u0007');
                    terminal.flush();
                }
            } else {
                // Second tab: display available options neatly
                tabFlag = false;
                terminal.writer().println();

                for (Candidate c : candidates) {
                    terminal.writer().print(c.value() + "  ");
                }

                terminal.writer().println();
                terminal.flush();

                // Clean up prompt rendering layout
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