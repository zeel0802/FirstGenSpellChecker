import java.io.*;
import java.util.*;

public class SpellChecker {
    private Set<String> dictionary;

    public SpellChecker(String dictionaryFile) throws IOException {
        dictionary = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile));
        String word;
        while ((word = reader.readLine()) != null) {
            dictionary.add(word.trim().toLowerCase());
        }
        reader.close();
    }

    public boolean isCorrect(String word) {
        return dictionary.contains(word.toLowerCase());
    }

    public String suggestCorrection(String input) {
        String bestSuggestion = null;
        int minDistance = Integer.MAX_VALUE;
        int minLengthDiff = Integer.MAX_VALUE;

        for (String dictWord : dictionary) {
            int distance = Levenshtein.computeDistance(input.toLowerCase(), dictWord);
            int lengthDiff = Math.abs(input.length() - dictWord.length());

            if (distance < minDistance ||
               (distance == minDistance && lengthDiff < minLengthDiff)) {
                minDistance = distance;
                minLengthDiff = lengthDiff;
                bestSuggestion = dictWord;
            }
        }

        if (minDistance <= 2) {
            return bestSuggestion;
        } else {
            return input;
        }
    }

    public static void main(String[] args) throws IOException {
        SpellChecker sc = new SpellChecker("dictionary.txt");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose mode: (1) Suggestions Only or (2) Auto-Correct");
        String mode = scanner.nextLine();
        boolean autoCorrect = mode.trim().equals("2");

        System.out.println("Paste your paragraph below and type 'END' on a new line when finished:");

        StringBuilder inputBuilder = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.trim().equalsIgnoreCase("END")) {
                break;
            }
            inputBuilder.append(line).append("\n");
        }

        String input = inputBuilder.toString();
        StringBuilder correctedParagraph = new StringBuilder();

        if (autoCorrect) {
            String[] tokens = input.split("(?<=\\s|(?=[,.!?]))");
            for (String token : tokens) {
                String cleanWord = token.replaceAll("[^a-zA-Z]", "");
                if (cleanWord.isEmpty()) {
                    correctedParagraph.append(token);
                    continue;
                }
                String suggestion = sc.isCorrect(cleanWord) ? cleanWord : sc.suggestCorrection(cleanWord);
                if (Character.isUpperCase(token.trim().charAt(0))) {
                    suggestion = suggestion.substring(0, 1).toUpperCase() + suggestion.substring(1);
                }
                correctedParagraph.append(token.replaceAll(cleanWord, suggestion));
            }

            System.out.println("\n--- Final Auto-Corrected Output ---");
            System.out.println(correctedParagraph.toString().trim());
        } else {
            System.out.println("\n--- Spell Check Suggestions ---");
            String[] tokens = input.split("\\s+");
            for (String token : tokens) {
                String cleanWord = token.replaceAll("[^a-zA-Z]", "");
                if (cleanWord.isEmpty()) continue;

                if (sc.isCorrect(cleanWord)) {
                    System.out.println("'" + cleanWord + "' is correct.");
                } else {
                    String suggestion = sc.suggestCorrection(cleanWord);
                    if (!suggestion.equals(cleanWord)) {
                        System.out.println("'" + cleanWord + "' is incorrect. Suggested correction: '" + suggestion + "'");
                    } else {
                        System.out.println("'" + cleanWord + "' is incorrect. No good suggestion found.");
                    }
                }
            }
        }
    }
}

class Levenshtein {
    public static int computeDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                        dp[i - 1][j - 1],
                        Math.min(dp[i - 1][j], dp[i][j - 1])
                    );
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }
}