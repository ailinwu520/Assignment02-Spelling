
import java.io.IOException;

public class A2 {

    /**
     * Prints suggestions for “onomatopoeia” as calculated by
     * {@link Spelling#suggest(java.lang.String, int)}.
     *
     * @param args {@code 0} - path to the token frequency file (see
     * {@link Spelling#Spelling(java.lang.String)}); {@code 1} - {@code count} argument to
     * {@link Spelling#suggest(java.lang.String, int)}
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final String wordFreqPath = args[0];
        final int count = Integer.parseInt(args[1]);

        final Spelling spelling = new Spelling(wordFreqPath);
        Spelling.printSuggest(spelling.suggest("onomatopoeia", count));
    }

}