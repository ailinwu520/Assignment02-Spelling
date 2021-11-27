import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Bounded number of greatest elements.
 *
 * This bag stores {@code Math.min(n, getMaxSize())} greatest elements from {@code n} elements that
 * were {@link #add(java.lang.Comparable)}ed into it. Hence the size of this bag is always less than
 * or equal to {@link #getMaxSize()}.
 *
 * @param <E> element type
 */
public class BagOfGreatest<E extends Comparable<E>> {

    private final int maxSize;
    private final PriorityQueue<E> elements = new PriorityQueue<>();

    /**
     *
     * @param maxSize must be positive
     */
    public BagOfGreatest(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Tries to insert {@code e} into this bag.
     *
     * Similar to {@link Queue#add(java.lang.Object)}, but obeys the size limit.
     *
     * @param e
     */
    public void add(E e) {
        if (elements.size() == maxSize) {
            /* This bag is full. */
            if (elements.peek().compareTo(e) < 0) {
                /* The least element in this collection is less than {@code e}.
                Replaces the least element in this collection with {@code e}. */
                elements.poll();
                elements.add(e);
            }
        } else elements.add(e);
    }

    /**
     * Retrieves and removes the least element of this bag.
     *
     * Similar to {@link PriorityQueue#poll()}.
     *
     * @return {@code null} if this bag is empty
     */
    public E poll() {
        return elements.poll();
    }

}

