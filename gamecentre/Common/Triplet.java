package group_0522.csc207.gamecentre.Common;

/**
 * A triplet to store three Object. The order of element matters.
 * @param <T> the first Object
 * @param <T1> the second Object
 * @param <T2> the third Object
 */
public class Triplet<T, T1, T2> extends Tuple<T, T1> {
    /**
     * The third element
     */
    private T2 third;

    /**
     * Create a new Triplet
     *
     * @param f the first element
     * @param s the second element
     * @param t the third element
     */
    public Triplet(T f, T1 s, T2 t) {
        super(f, s);
        this.third = t;
    }


    /**
     * Return the third element
     *
     * @return third
     */
    public T2 getThird() {
        return this.third;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Triplet)) {
            return false;
        }
        Triplet other = (Triplet) o;
        return (this.getFirst().equals(other.getFirst()) && this.getSecond().equals(other.getSecond()) && this.third.equals(other.getThird()));
    }
}
