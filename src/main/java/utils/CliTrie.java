package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CliTrie {
    private static class TrieNode {
        public boolean isFinish = false;
        public HashMap<Character, TrieNode> nodes = new HashMap<>();
        public String word = null;
    }

    private static final TrieNode root;

    static {
        root = new TrieNode();

        // build the trie with the builtin commands
        String[] builtin = Registry.getBuiltin();
        for(String s: builtin) {
            push(s);
        }

        // now push all the executables in the PATH
        final String path = System.getenv("PATH");
        final String[] dirs = path.split(Pattern.quote(File.pathSeparator));

        for (String d: dirs) {
            Path dpath = Paths.get(d);

            if (!Files.exists(dpath)) continue;

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dpath)) {
                for (Path e: stream) {
                    if (!Files.isExecutable(e)) continue;

                    // if exists and executable then push into the DataStructure
                    push(e.getFileName().toString());
                }
            }catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void push(String word) {
        TrieNode curr = root;

        for (char c : word.toCharArray()) {
            curr.nodes.computeIfAbsent(c, k -> new TrieNode());
            curr = curr.nodes.get(c);
        }

        curr.isFinish = true;
        curr.word = word;
    }

    public static List<String> getCandidates(String word) {
        TrieNode curr = root;

        for (char c : word.toCharArray()) {
            if (!curr.nodes.containsKey(c)) {
                return new ArrayList<>();
            }
            curr = curr.nodes.get(c);
        }

        List<String> ans = new ArrayList<>();
        dfs(curr, ans);

        return ans;
    }

    // this return the largest common prefix and also a flag that indicate if its complete
    public static Pair<String, Boolean> getLCP(String prefix) {
        TrieNode curr = root;
        StringBuilder sb = new StringBuilder();

        for (char c: prefix.toCharArray()) {
            if (!curr.nodes.containsKey(c)) return new Pair<>("", false);

            sb.append(c);
            curr = curr.nodes.get(c);
        }

        if (curr.nodes.size() != 1) return new Pair<>("", false);

        while (curr != null && curr.nodes.size() == 1) {
            if (curr.isFinish) return new Pair<>(curr.word, false);
            for (Map.Entry<Character, TrieNode> nextNode: curr.nodes.entrySet()) {
                sb.append(nextNode.getKey());
                curr = nextNode.getValue();
            }
        }
        if (curr != null && curr.isFinish) return new Pair<>(curr.word, curr.isFinish);
        return new Pair<>(sb.toString(), false);
    }

    private static void dfs(TrieNode node, List<String> ans) {
        if (node == null) return;
        if (node.isFinish) ans.add(node.word);

        for (TrieNode childNode : node.nodes.values()) {
            dfs(childNode, ans);
        }
    }
}
