package io.adaptor.autocompletion;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.ParsedLine;

public interface ShellCompleter extends Completer {
    Candidate getLargetCommonPrefix(ParsedLine parsedLine);
}
