package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ArgumentParser {
    private static HashSet<Character> special;

    static {
        special = new HashSet<>();

        // set of special characters
        special.add('$');
        special.add('"');
        special.add('\'');
        special.add('`');
        special.add('\\');
        special.add('\n');
    }

    public static String[] getArgs(String text) {
        text = text.strip();
        List<String> tokens = new ArrayList<>();

        int i = 0;
        int n = text.length();

        while (i < n) {
            StringBuilder sb = new StringBuilder();
            while (i < n && text.charAt(i) != ' ') {
                if (text.charAt(i) == '\\') {
                    if (++i < n) sb.append(text.charAt(i));
                } else if (text.charAt(i) == '\'' || text.charAt(i) == '"') {
                    char quote = text.charAt(i);
                    int k = sb.length();

                    i++; // skip the quote
                    while (i < n && text.charAt(i) != quote) {
                        if (quote == '"' && text.charAt(i) == '\\'
                                && special.contains(text.charAt(i+1))
                        ) {
                            ++i;
                        }
                        sb.append(text.charAt(i));
                        i++;
                    }

                    if (i == n) sb.insert(k, quote); // if the quote is single the insert it as a char
                } else {
                    sb.append(text.charAt(i));
                }
                i++;
            }

            while (i < n && text.charAt(i) == ' ') i++;
            tokens.add(sb.toString());
        }

        return tokens.toArray(new String[0]);
    }
}
