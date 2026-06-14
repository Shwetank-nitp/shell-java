package utils;

import java.util.ArrayList;
import java.util.List;

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
        // later i could add the path files and dir names

        String[] builtin = Registry.getBuiltin();
        for(String s: builtin) {
            push(s);
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
                return new ArrayList<>(List.of(word));
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
