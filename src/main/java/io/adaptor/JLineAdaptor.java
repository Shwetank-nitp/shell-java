package io.adaptor;

import org.jline.reader.*;
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
                .build();

        ApplyTabWidget tabWidget = getApplyTabWidget(terminal, parser);

        reader.getWidgets().put("shell-tab", tabWidget);

        reader.getKeyMaps()
                .get(LineReader.MAIN)
                .bind(new Reference("shell-tab"), "\t");
    }

    private ApplyTabWidget getApplyTabWidget(Terminal terminal, DefaultParser parser) {
        TabCompletion executable = new TabCompletion(
                reader,
                terminal,
                parser,
                new ExecutableCompleter()
        );

        TabCompletion fileSystem = new TabCompletion(
                reader,
                terminal,
                parser,
                new FileSystemCompleter()
        );

        return new ApplyTabWidget(
                reader,
                executable,
                fileSystem,
                parser
        );
    }

    @Override
    public String ioRead() {
        return reader.readLine("$ ");
    }
}