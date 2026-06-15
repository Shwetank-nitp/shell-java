package io.adaptor;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import utils.CliTrie;
import utils.Pair;

import java.util.List;

public class ShellCompleter implements Completer {
    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        String currToken = parsedLine.word();

        List<String> possibleCandidate = CliTrie.getCandidates(currToken);

        for (String candidateX: possibleCandidate) {
            list.add(
                    new CandidateBuilder(candidateX)
                            .suffix(candidateX.substring(currToken.length()))
                            .complete(true)
                            .build()
            );
        }
    }

    public Candidate getBestPrefix(LineReader lineReader, ParsedLine parsedLine) {
        String currToken = parsedLine.word();
        Pair<String, Boolean> lcp = CliTrie.getLCP(currToken);

        String suffix = lcp.first().startsWith(currToken)
                ? lcp.first().substring(currToken.length())
                : "";

        return new CandidateBuilder(lcp.first())
                .complete(lcp.second())
                .suffix(suffix)
                .build();
    }
}
