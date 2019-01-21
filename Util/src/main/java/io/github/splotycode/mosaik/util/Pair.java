package io.github.splotycode.mosaik.util;

public class Pair <A, B> {

    public static final Pair<?, ?> EMPTY = new Pair<>(null, null);

    public static <A, B> Pair<A, B> getEmpty() {
        return (Pair<A, B>) EMPTY;
    }

    private A one;
    private B two;

    public Pair(A one, B two) {
        this.one = one;
        this.two = two;
    }

    public A getOne() {
        return one;
    }

    public void setOne(A one) {
        this.one = one;
    }

    public B getTwo() {
        return two;
    }

    public void setTwo(B two) {
        this.two = two;
    }
}
