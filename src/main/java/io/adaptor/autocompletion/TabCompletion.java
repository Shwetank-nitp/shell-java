package io.adaptor.autocompletion;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabCompletion implements Widget {
    private final LineReader reader;
    private final Terminal terminal;
    private final DefaultParser parser;
    private final ShellCompleter completer;

    // State Variables
    private boolean isSecondTabPress = false;
    private String lastLookedUpWord = "";

    public TabCompletion(LineReader reader, Terminal terminal, DefaultParser parser, ShellCompleter completer) {
        this.reader = reader;
        this.terminal = terminal;
        this.parser = parser;
        this.completer = completer;
    }

    @Override
    public boolean apply() {
        Buffer buffer = reader.getBuffer();
        String currentBufferContent = buffer.toString();
        int cursorPosition = buffer.cursor();

        ParsedLine parsedLine = parser.parse(currentBufferContent, cursorPosition, Parser.ParseContext.COMPLETE);
        String currentWord = parsedLine.word();

        // Reset state machine if the user modified the text since the last tab press
        if (!currentWord.equals(lastLookedUpWord)) {
            isSecondTabPress = false;
        }
        lastLookedUpWord = currentWord;

        Candidate bestMatch = completer.getLargetCommonPrefix(parsedLine);

        // Scenario A: No matches found at all -> Ring terminal bell
        if (bestMatch.value().isEmpty() && !isSecondTabPress) {
            isSecondTabPress = true;
            triggerTerminalAlert();
            return true;
        }

        // Scenario B: First Tab Press -> Try to fill the Longest Common Prefix
        if (!isSecondTabPress) {
            String suffixToAppend = bestMatch.suffix();
            String trailingSpace = bestMatch.complete() ? " " : "";
            buffer.write(suffixToAppend + trailingSpace);

            isSecondTabPress = true;
        }
        // Scenario C: Second Tab Press -> Display all available options neatly
        else {
            isSecondTabPress = false;
            displayAvailableCandidates(parsedLine);
        }
        return true;
    }

    private void displayAvailableCandidates(ParsedLine parsedLine) {
        List<Candidate> candidates = new ArrayList<>();
        completer.complete(reader, parsedLine, candidates);

        if (candidates.isEmpty()) {
            triggerTerminalAlert();
            return;
        }

        Collections.sort(candidates);

        terminal.writer().println();
        for (Candidate candidate : candidates) {
            terminal.writer().print(candidate.value() + "  ");
        }
        terminal.writer().println();
        terminal.flush();

        // Force JLine to redraw the command line cleanly below the printed options
        reader.callWidget(LineReader.REDRAW_LINE);
        reader.callWidget(LineReader.REDISPLAY);
    }

    private void triggerTerminalAlert() {
        terminal.writer().write('\u0007'); // ASCII Bell character
        terminal.flush();
    }
}
