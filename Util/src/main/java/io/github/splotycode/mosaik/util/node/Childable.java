package io.github.splotycode.mosaik.util.node;

import java.util.ArrayList;
import java.util.Collection;

public interface Childable<I> {

    Collection<I> getChildes();

    default void addChild(I child) {
        getChildes().add(child);
    }

    default Collection<I> getAllChilds() {
        Collection<I> childes = new ArrayList<>();
        for (I child : getChildes()) {
            getAllChildes0(childes, child);
        }
        return childes;
    }

    default void getAllChildes0(Collection<I> list, I next) {
        list.add(next);
        if (next instanceof Childable) {
            for (Object child : ((Childable) next).getChildes()) {
                try {
                    getAllChildes0(list, (I) child);
                } catch (ClassCastException ignore) {}
            }
        }
    }

}
