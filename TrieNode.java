
/**
 * Trie where keys are strings of alphabet characters (see {@link Spelling}) and values are
 * {@link TokenInfo}.
 */
public class TrieNode {

    /**
     * May be {@code null}.
     */
    private TokenInfo value;

    private TrieChildren children;

    /**
     * Get a child, inserting it if needed.
     *
     * If there is no child at index {@code i}, inserts an empty child there. Then returns the child
     * at index {@code i}.
     *
     * @param i
     * @return isn't {@code null}
     */
    private TrieNode getChildInsert(int i) {
        TrieNode childNode = children.get(i);
        if (childNode == null) {
            final TrieChildrenArray children1;
            if (children instanceof TrieChildrenNone) {
                children1 = new TrieChildrenArray();
                children = children1;
            } else children1 = (TrieChildrenArray) children;

            childNode = new TrieNode();
            children1.set(i, childNode);
        }
        return childNode;
    }

    /**
     * Creates an empty trie.
     */
    public TrieNode() {
        children = TrieChildrenNone.A;
    }

    /**
     *
     * @param c
     * @return the child at character {@code c}; may be {@code null}
     */
    public TrieNode getChild(char c) {
        return children.get(Spelling.charToI(c));
    }

    /**
     * Inserts a record.
     *
     * Deletes the existing record with key {@code token} if it exists. Inserts a record with key
     * {@code token} and {@code value}.
     *
     * @param token
     * @param value
     */
    public void insert(String token, TokenInfo value) {
        TrieNode node = this;
        for (int i = 0; i < token.length(); i++) {
            final int childI = Spelling.charToI(token.charAt(i));
            if (childI < 0) throw new RuntimeException();
            node = node.getChildInsert(childI);
        }
        node.value = value;
    }

    public interface TokenRecordConsumer {

        void consume(String token, TokenInfo info);

    }

    /**
     * Collects descendants.
     *
     * For every descendant node containing a value, calls
     * {@link TokenRecordConsumer#consume(java.lang.String, TokenInfo)} of {@code consumer} as
     * {@code consume(token1, value)} where {@code value} is the value of the descendant and
     * {@code token1} is the concatenation of {@code token} and the path to the descendant.
     *
     * After the call, {@code token} will contain the same content as before the call.
     *
     * @param token
     * @param consumer
     */
    public void collect(StringBuilder token, TokenRecordConsumer consumer) {
        if (value != null) consumer.consume(token.toString(), value);
        for (int i = 0; i < Spelling.CHAR_N; i++) {
            final TrieNode node = children.get(i);
            if (node != null) {
                token.append(Spelling.iToChar(i));
                node.collect(token, consumer);
                token.deleteCharAt(token.length() - 1);
            }
        }
    }

}