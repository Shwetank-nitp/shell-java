package io.adaptor;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JLineAdaptor implements IOAdapter {

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
                .completer(new ShellCompleter())
                .build();
    }

    @Override
    public String ioRead() {
        return reader.readLine("$ ");
    }
}