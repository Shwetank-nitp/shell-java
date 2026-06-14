package io.adaptor;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import utils.CliTrie;

import java.util.List;

public class ShellCompleter implements Completer {
    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        String currToken = parsedLine.word();

        List<String> possibleCandidate = CliTrie.getCandidates(currToken);

        for (String candidateX: possibleCandidate) {
            list.add(new Candidate(candidateX));
        }
    }
}
