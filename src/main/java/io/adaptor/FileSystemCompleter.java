package io.adaptor;

import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import utils.CliTrie;
import utils.Pair;

import java.util.List;

public class FileSystemCompleter implements ShellCompleter {
    @Override
    public Candidate getLargetCommonPrefix(ParsedLine parsedLine) {
        String currToken = parsedLine.word();
        Pair<String, Boolean> lcp = CliTrie
                .getLongestCommonPrefixForFileSystemItem(currToken);

        String suffix = lcp.first().startsWith(currToken)
                ? lcp.first().substring(currToken.length())
                : "";

        return new CandidateBuilder(lcp.first())
                .complete(lcp.second())
                .suffix(suffix)
                .build();
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String currToken = line.word();

        List<String> possibleCandidate = CliTrie
                .findCandidatesForFileSystemItem(currToken);

        for (String candidateX: possibleCandidate) {
            candidates.add(
                    new CandidateBuilder(candidateX)
                            .suffix(candidateX.substring(currToken.length()))
                            .complete(true)
                            .build()
            );
        }
    }
}
