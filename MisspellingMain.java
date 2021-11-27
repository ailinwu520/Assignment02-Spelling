
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MisspellingMain {

    private static final class Misspelling {

        private final String correctToken;
        private final String misspelledToken;

        public Misspelling(String correctToken, String misspelledToken) {
            this.correctToken = correctToken;
            this.misspelledToken = misspelledToken;
        }

        public String getCorrectToken() {
            return correctToken;
        }

        public String getMisspelledToken() {
            return misspelledToken;
        }

    }

    /**
     * Prints suggestions for misspelled tokens in file {@code args[1]} as calculated by
     * {@link Spelling#suggest(java.lang.String, int)}.
     *
     * {@code args[1]} must be a CSV file with two columns (correct token, misspelled token) and a
     * header row. Uppercase Latin letters in this file will be converted to lowercase.
     *
     * @param args {@code 0} - path to the token frequency file (see
     * {@link Spelling#Spelling(java.lang.String)}); {@code 1} - see above
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final String wordFreqPath = args[0];
        final String misspellingPath = args[1];

        final List<Misspelling> misspellings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(misspellingPath))) {
            final String line1 = reader.readLine();
            if (line1 != null)
                while (true) {
                    final String line = reader.readLine();
                    if (line == null) break;
                    String[] fields = Spelling.COMMA.split(line);
                    misspellings.add(
                            new Misspelling(fields[0].toLowerCase(), fields[1].toLowerCase()));

                }
        }

        final Spelling spelling = new Spelling(wordFreqPath);
        for (int count = 3; count <= 7; count++) {
            System.out.println();
            System.out.println("######## The number of suggested tokens: " + count);
            int suggestCorrectN = 0;
            for (Misspelling misspelling : misspellings) {
                final List<List<String>> suggest0
                        = spelling.suggest(misspelling.getMisspelledToken(), count);
                final boolean didSuggestCorrectly0
                        = Spelling.didSuggestCorrectly(suggest0, misspelling.getCorrectToken());
                if (didSuggestCorrectly0) suggestCorrectN++;

                System.out.println();
                System.out.println("Suggestions for “" + misspelling.getMisspelledToken()
                        + "” which is misspelled “" + misspelling.getCorrectToken() + "”"
                        + (didSuggestCorrectly0 ? " (contains the correct suggestion)" : "")
                        + ":");
                Spelling.printSuggest(suggest0);
            }
            System.out.println();
            System.out.println("The number of misspelled tokens for which there was"
                    + " a correct suggestion: " + suggestCorrectN);
        }
    }

}
