package io.adaptor.autocompletion;

import io.adaptor.CandidateBuilder;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import utils.CliTrie;
import utils.Pair;

import java.util.List;

public class FileSystemCompleter implements ShellCompleter {

    private static Pair<String, String> splitDirAndPrefix(String currToken) {
        StringBuilder dir = new StringBuilder(currToken);
        while (!dir.isEmpty() && dir.charAt(dir.length() - 1) != '/') {
            dir.setLength(dir.length() - 1);
        }
        String prefix = currToken.substring(dir.length());
        return new Pair<>(dir.toString(), prefix);
    }

    @Override
    public Candidate getLargetCommonPrefix(ParsedLine parsedLine) {
        String currToken = parsedLine.word();
        Pair<String,String> parts = splitDirAndPrefix(currToken);
        String dir = parts.first();
        String prefix = parts.second();

        Pair<String, Boolean> lcp;
        try {
            lcp = CliTrie.getLongestCommonPrefixForFileSystemItem(prefix, dir);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String suffix = lcp.first().startsWith(prefix)
                ? lcp.first().substring(prefix.length())
                : "";

        return new CandidateBuilder(dir + lcp.first())
                .complete(lcp.second())
                .suffix(suffix)
                .build();
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String currToken = line.word();
        Pair<String,String> parts = splitDirAndPrefix(currToken);
        String dir = parts.first();
        String prefix = parts.second();

        List<String> possibleCandidates;
        try {
            possibleCandidates = CliTrie.findCandidatesForFileSystemItem(prefix, dir);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (String candidateX : possibleCandidates) {
            candidates.add(
                    new CandidateBuilder(dir + candidateX)
                            .suffix(candidateX.substring(prefix.length()))
                            .complete(true)
                            .build()
            );
        }
    }
}