package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CliTrie {
    private static class TrieNode {
        public boolean isFinish = false;
        public TrieNode[] nodes = new TrieNode[226]; // 8bit trie for simple cli without Unicode

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

        for(char c : word.toCharArray()) {
            if (curr.nodes[c] == null) {
                curr.nodes[c] = new TrieNode();
            }

            curr = curr.nodes[c];
        }

        curr.isFinish = true;
        curr.word = word;
    }

    public static List<String> getCandidates(String word) {
        TrieNode curr = root;

        for (char c : word.toCharArray()) {
            if (curr.nodes[c] == null) {
                return new ArrayList<>();
            }

            curr = curr.nodes[c];
        }
        List<String> ans = new ArrayList<>();
        dfs(curr, ans);

        return ans;
    }

    private static void dfs(TrieNode node, List<String> ans) {
        if (node == null) return;
        if (node.isFinish) ans.add(node.word);

        for(int i = 0; i < node.nodes.length; i++) {
            if (node.nodes[i] == null) continue;
            dfs(node.nodes[i], ans);
        }
    }
}
