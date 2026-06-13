package utils;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {

    public static String[] getArgs(String text) {
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
