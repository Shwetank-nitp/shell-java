package io.adaptor;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;

public class ApplyTabWidget implements Widget {
    private final LineReader reader;
    private final TabCompletion exec;
    private final TabCompletion fSys;
    private final DefaultParser parser;

    public ApplyTabWidget(LineReader reader, TabCompletion exec, TabCompletion fSys, DefaultParser parser) {
        this.reader = reader;
        this.exec = exec;
        this.fSys = fSys;
        this.parser = parser;
    }

    @Override
    public boolean apply() {
        Buffer buffer = reader.getBuffer();
        String currentBufferContent = buffer.toString();
        int cursorPosition = buffer.cursor();

        ParsedLine parsedLine = parser.parse(
                currentBufferContent,
                cursorPosition,
                Parser.ParseContext.COMPLETE
        );

        int choose = parsedLine.wordIndex() > 0 ? 1 : 0;

        return switch (choose) {
            case 0 -> exec.apply();
            case 1 -> fSys.apply();
            default -> throw new IllegalStateException("Unexpected value: " + choose);
        };
    }
}
