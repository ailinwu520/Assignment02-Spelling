import java.util.Iterator;

/**
 * Children stored in an array.
 */
public class TrieChildrenArray extends TrieChildren {

    /**
     * The children of this node.
     *
     * The size must be {@link Spelling#CHAR_N}. Elements may be {@code null}.
     */
    public TrieNode[] children = new TrieNode[Spelling.CHAR_N];

    @Override
    public TrieNode get(int i) {
        return children[i];
    }

    public void set(int i, TrieNode node) {
        children[i] = node;
    }

    private static class Iterator1 implements Iterator<TrieNode> {

        private final int i;
        private final TrieNode[] children;

        private int findAfter(int i) {
            for (; i != Spelling.CHAR_N; i++)
                if (children[i] != null) return i;
            return i;
        }

        public Iterator1(TrieNode[] children) {
            this.i = findAfter(0);
            this.children = children;
        }

        @Override
        public boolean hasNext() {
            return i != Spelling.CHAR_N;
        }

        @Override
        public TrieNode next() {
            return children[i];
        }
    }

    @Override
    public Iterator<TrieNode> iterator() {
        return new Iterator1(children);
    }

}
