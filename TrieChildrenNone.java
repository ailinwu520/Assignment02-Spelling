
import java.util.Iterator;

/**
 * Empty list of children.
 */
public class TrieChildrenNone extends TrieChildren {

    private static final Iterator1 ITERATOR = new Iterator1();

    public static final TrieChildrenNone A = new TrieChildrenNone();

    @Override
    public TrieNode get(int i) {
        return null;
    }

    private static class Iterator1 implements Iterator {

        public Iterator1() {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public TrieNode next() {
            return null;
        }

    }

    @Override
    public Iterator<TrieNode> iterator() {
        return ITERATOR;
    }

}
