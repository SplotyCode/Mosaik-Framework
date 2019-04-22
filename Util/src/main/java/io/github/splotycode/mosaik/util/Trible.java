package io.github.splotycode.mosaik.util;

/**
 * @deprecated use {@link Tribble}
 */
@Deprecated
public class Trible<A, B, C> {

    private A one;
    private B two;
    private C tree;

    public Trible(A one, B two, C tree) {
        this.one = one;
        this.two = two;
        this.tree = tree;
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

    public C getTree() {
        return tree;
    }

    public void setTree(C tree) {
        this.tree = tree;
    }
}
