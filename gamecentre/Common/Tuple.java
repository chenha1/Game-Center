package group_0522.csc207.gamecentre.Common;

import java.io.Serializable;

/**
 *  A tuple to store two Object. The order of element matters.
 *
 * @param <T> the first Object
 * @param <T1> the second Object
 */
public class Tuple<T, T1> implements Serializable {
    /**
     * The first element
     */
    private T first;
    /**
     * The second element
     */
    private T1 second;

    /**
     * Create a tuple
     *
     * @param f the first element
     * @param s the second element
     */
    public Tuple(T f, T1 s) {
        this.first = f;
        this.second = s;

    }

    /**
     * Return the first element
     *
     * @return the first element
     */
    public T getFirst() {
        return this.first;
    }

    /**
     * Return the second element
     *
     * @return the second element
     */
    public T1 getSecond() {
        return this.second;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple)) {
            return false;
        }
        Tuple other = (Tuple) o;
        return (this.first.equals(other.getFirst()) && this.second.equals(other.getSecond()));
    }
}
