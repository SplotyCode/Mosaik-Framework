package io.github.splotycode.mosaik.util.node;

import java.util.ArrayList;
import java.util.Collection;

public interface Childable<I extends Childable<I>> {

    Collection<I> getChildes();

    default Collection<I> getAllChilds() {
        Collection<I> childes = new ArrayList<>();
        for (I child : getChildes()) {
            getAllChildes0(childes, child);
        }
        return childes;
    }

    default void getAllChildes0(Collection<I> list, I next) {
        list.add(next);
        for (I child : next.getChildes()) {
            getAllChildes0(list, child);
        }
    }

}
