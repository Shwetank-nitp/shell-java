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

        ShellCompleter completer = new ShellCompleter();

        final Widget tabAutoCompletion = () -> {
            Buffer buffer = reader.getBuffer();
            String currBuffer = buffer.toString();
            int cursor = buffer.cursor();

            // Parse context-aware using the exact cursor position
            ParsedLine parsedLine = parser.parse(currBuffer, cursor, Parser.ParseContext.COMPLETE);
            String currentWord = parsedLine.word();

            Candidate bestMatch = completer.getBestPrefix(reader, parsedLine);

            // If candidate best match is empty ring the BELL
            if (bestMatch.value().isEmpty() && !tabFlag) {
                tabFlag = true;
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

            if (!tabFlag) {  // If first tab and a LCP is found the complete it
                buffer.write(bestMatch.suffix() + (bestMatch.complete() ? " " : ""));
                tabFlag = true;
            } else {
                // Second tab: display available options neatly
                tabFlag = false;
                terminal.writer().println();

                List<Candidate> candidates = new ArrayList<>();
                completer.complete(reader, parsedLine, candidates);
                Collections.sort(candidates);

                for (Candidate c : candidates) {
                    terminal.writer().print(c.value() + "  ");
                }

                terminal.writer().println();
                terminal.flush();

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