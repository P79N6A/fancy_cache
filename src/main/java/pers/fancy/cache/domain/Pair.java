package pers.fancy.cache.domain;


/**
 * @author fancy
 */
public class Pair<L, R> {

    private L left;

    private R right;

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
}
