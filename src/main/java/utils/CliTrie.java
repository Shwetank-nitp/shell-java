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
        public boolean isEndOfWord = false;
        public HashMap<Character, TrieNode> children = new HashMap<>();
        public String completeWord = null;
    }

    private static final TrieNode EXECUTABLE_TREE_ROOT;
    private static final TrieNode FILE_SYSTEM_TREE_ROOT;

    static {
        EXECUTABLE_TREE_ROOT = new TrieNode();
        FILE_SYSTEM_TREE_ROOT = new TrieNode();

        String currentDirectory = System.getProperty("user.dir");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(currentDirectory))) {
            for (Path filePath : stream) {
                insert(filePath.getFileName().toString(), FILE_SYSTEM_TREE_ROOT);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to initialize file system autocomplete cache", ex);
        }

        String[] builtinCommands = Registry.getBuiltin();
        for (String command : builtinCommands) {
            insert(command, EXECUTABLE_TREE_ROOT);
        }

        String environmentPath = System.getenv("PATH");
        if (environmentPath != null) {
            String[] pathDirectories = environmentPath.split(Pattern.quote(File.pathSeparator));

            for (String directory : pathDirectories) {
                Path dirPath = Paths.get(directory);

                if (!Files.exists(dirPath)) {
                    continue;
                }

                try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
                    for (Path executablePath : stream) {
                        if (Files.isExecutable(executablePath)) {
                            insert(executablePath.getFileName().toString(), EXECUTABLE_TREE_ROOT);
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException("Failed to read PATH directory: " + directory, ex);
                }
            }
        }
    }

    private static void insert(String word, TrieNode rootNode) {
        TrieNode current = rootNode;

        for (char ch : word.toCharArray()) {
            current.children.computeIfAbsent(ch, k -> new TrieNode());
            current = current.children.get(ch);
        }

        current.isEndOfWord = true;
        current.completeWord = word;
    }

    private static List<String> fetchSuggestions(String prefix, TrieNode rootNode) {
        TrieNode current = rootNode;

        for (char ch : prefix.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return new ArrayList<>();
            }
            current = current.children.get(ch);
        }

        List<String> suggestions = new ArrayList<>();
        gatherAllWordsFromNode(current, suggestions);
        return suggestions;
    }

    private static void gatherAllWordsFromNode(TrieNode node, List<String> resultList) {
        if (node == null) return;
        if (node.isEndOfWord) {
            resultList.add(node.completeWord);
        }

        for (TrieNode childNode : node.children.values()) {
            gatherAllWordsFromNode(childNode, resultList);
        }
    }

    private static Pair<String, Boolean> computeLongestCommonPrefix(String prefix, TrieNode rootNode) {
        TrieNode current = rootNode;
        StringBuilder commonPrefixBuilder = new StringBuilder();

        // Navigate to the end of the user's typed prefix string
        for (char ch : prefix.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return new Pair<>("", false);
            }
            commonPrefixBuilder.append(ch);
            current = current.children.get(ch);
        }

        // Note: this is not a BUG
        // Returning the empty String means that you cannot further complete the string
        if (current.children.size() != 1) {
            return new Pair<>("", false);
        }

        // Auto-advance down the chain as long as there is exactly 1 unique path forward
        while (current != null && current.children.size() == 1) {
            if (current.isEndOfWord) {
                return new Pair<>(current.completeWord, false);
            }
            for (Map.Entry<Character, TrieNode> nextNode : current.children.entrySet()) {
                commonPrefixBuilder.append(nextNode.getKey());
                current = nextNode.getValue();
            }
        }

        if (current != null && current.isEndOfWord) {
            return new Pair<>(current.completeWord, true);
        }

        return new Pair<>(commonPrefixBuilder.toString(), false);
    }

    public static void insertExecutable(String command) {
        insert(command, EXECUTABLE_TREE_ROOT);
    }

    public static void insertFileSystemItem(String fileName) {
        insert(fileName, FILE_SYSTEM_TREE_ROOT);
    }

    public static List<String> findCandidatesForExecutable(String prefix) {
        return fetchSuggestions(prefix, EXECUTABLE_TREE_ROOT);
    }

    public static List<String> findCandidatesForFileSystemItem(String prefix) {
        return fetchSuggestions(prefix, FILE_SYSTEM_TREE_ROOT);
    }

    public static Pair<String, Boolean> getLongestCommonPrefixForExecutable(String prefix) {
        return computeLongestCommonPrefix(prefix, EXECUTABLE_TREE_ROOT);
    }

    public static Pair<String, Boolean> getLongestCommonPrefixForFileSystemItem(String prefix) {
        return computeLongestCommonPrefix(prefix, FILE_SYSTEM_TREE_ROOT);
    }
}