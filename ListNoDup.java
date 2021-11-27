
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * List without duplicates.
 *
 * It differs from set in that it preserves the order in which elements were added.
 */
public class ListNoDup<E> {

    private final Set<E> set = new HashSet<>();
    private List<E> list = new ArrayList<>();

    /**
     * Adds {@code e} to the end of this list if and only if this list doesn't already contain
     * {@code e}.
     *
     * @param e
     */
    public void add(E e) {
        if (!set.contains(e)) {
            list.add(e);
            set.add(e);
        }
    }

    public int size() {
        return list.size();
    }

    /**
     * Retrieves and removes the contents of this list.
     *
     * @return the contents of this list
     */
    public List<E> extract() {
        final List<E> result = list;
        list = new ArrayList<>();
        return result;
    }

}
