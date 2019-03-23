package io.github.splotycode.mosaik.util.node;

import java.util.ArrayList;
import java.util.Collection;

public interface Parentable<I extends Parentable<I>> {

    I getParent();

    default I getHeadParent() {
        I parent = getParent();
        while (parent.getParent() != null) {
            parent = parent.getParent();
        }
        return parent;
    }

    default Collection<I> getAllParents() {
        Collection<I> all = new ArrayList<>();
        I parent = getParent();
        while (parent != null) {
            all.add(parent);
            parent = parent.getParent();
        }
        return all;
    }

}
