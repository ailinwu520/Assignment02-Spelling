
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Suggests corrections for misspelled tokens.
 *
 * Tokens must be built from a restricted set of characters called the *alphabet*. The alphabet is
 * all lowercase English letters. The order of characters in the alphabet (needed in
 * {@link #charToI(char)} and {@link #iToChar(int)}) is the usual order of the English alphabet.
 */
public class Spelling {

    public static final Pattern COMMA = Pattern.compile(",");

    /**
     * the size of the alphabet
     */
    public static final int CHAR_N = 26;

    /**
     *
     * @param c
     * @return the index of {@code c} in the alphabet or {@code -1} if {@code c} doesn't belong to
     * the alphabet
     */
    public static int charToI(char c) {
        return 'a' <= c && c <= 'z' ? c - 'a' : -1;
    }

    /**
     *
     * @param i
     * @return the character at index {@code i} in the alphabet or {@code 0} if {@code i} is out of
     * bounds
     */
    public static char iToChar(int i) {
        return 0 <= i && i < CHAR_N ? (char) (i + 'a') : 0;
    }

    /**
     * Prints {@code suggest0} which should be the result of
     * {@link #suggest(java.lang.String, int)}.
     *
     * @param suggest0
     */
    public static void printSuggest(List<List<String>> suggest0) {
        for (List<String> a : suggest0) System.out.println(a);
    }

    /**
     *
     * @param suggest0
     * @param token
     * @return whether {@code suggest0} contains {@code token}
     */
    public static boolean didSuggestCorrectly(List<List<String>> suggest0, String token) {
        for (List<String> a : suggest0) if (a.contains(token)) return true;
        return false;
    }

    private TrieNode node;

    /**
     * Creates an object containing tokens and their frequencies from file {@code wordFreqPath}.
     *
     * {@code wordFreqPath} must be a CSV file with two columns (token, frequency (non-negative
     * integer)) and a header row.
     *
     * @param wordFreqPath
     * @throws IOException
     */
    public Spelling(String wordFreqPath) throws IOException {
        node = new TrieNode();

        try (BufferedReader reader = new BufferedReader(new FileReader(wordFreqPath))) {
            final String line1 = reader.readLine();
            if (line1 != null)
                while (true) {
                    final String line = reader.readLine();
                    if (line == null) break;
                    String[] fields = COMMA.split(line);
                    node.insert(fields[0], new TokenInfo(Long.parseLong(fields[1])));
                }
        }
    }

    /**
     * Suggests corrections for a misspelled token.
     *
     * In the returned list of prefix suggestions, the {@code i}th prefix suggestion is a list of
     * suggested tokens based on the prefix of {@code token} of length {@code i + 1}. Every prefix
     * suggestion contains at most {@code count} elements. Elements of a prefix suggestion are
     * stored in descending order of token frequency.
     *
     * @param token must be non-empty
     * @param count
     * @return the list of prefix suggestions for {@code token}
     */
    public List<List<String>> suggest(String token, int count) {
        if (token.length() == 0) throw new RuntimeException();

        /* This list will contain:
        - suggested tokens based on the prefix of {@code token} of length {@code 1} in
        ascending order of token frequency,
        - the same for length {@code 2},
        - and so on.
        Such a list will be useful when building prefix suggestions below. */
        final Deque<String> frequentTokens = new ArrayDeque<>();

        final List<List<String>> result = new ArrayList<>();
        TrieNode node1 = this.node;

        /* the index of a character in {@code token} */
        int i = 0;

        final StringBuilder token1 = new StringBuilder();

        /* Processes all prefixes of {@code token} of lengths from {@code 1} to
        {@code token.length()}. */
        while (true) {
            final char c = token.charAt(i);
            token1.append(c);

            /* Processes the prefix of {@code token} of length {@code i + 1} which is stored
            in {@code token1}. */
            if (charToI(c) < 0) throw new RuntimeException();
            if (node1 != null) node1 = node1.getChild(c);

            /* {@code node1} is the trie at path {@code token1} in {@link #node}. */
            if (node1 != null) {
                final BagOfGreatest<TokenRecord> frequentTokens1 = new BagOfGreatest<>(count);
                /* Collects token records with greatest frequencies in {@link #node}
                whose keys start with {@code token1} into {@code frequentTokens1}. */
                node1.collect(
                        token1,
                        new TrieNode.TokenRecordConsumer() {
                            @Override
                            public void consume(String token, TokenInfo info) {
                                frequentTokens1.add(new TokenRecord(token, info));
                            }
                        });

                /* Copies from {@code frequentTokens1} to {@code frequentTokens}. */
                while (true) {
                    final TokenRecord tokenRecord = frequentTokens1.poll();
                    if (tokenRecord == null) break;
                    frequentTokens.add(tokenRecord.getToken());
                }
            }

            /* Copies at most {@code count} elements from the end of {@code frequentTokens}
            into a new list and adds this new list to {@code result}. */
            final ListNoDup<String> frequentTokens1 = new ListNoDup<>();
            final Iterator<String> iter = frequentTokens.descendingIterator();
            while (frequentTokens1.size() < count && iter.hasNext())
                frequentTokens1.add(iter.next());
            result.add(frequentTokens1.extract());

            i++;
            if (i == token.length()) break;
        }
        return result;
    }

}
