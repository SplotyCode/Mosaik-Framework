package io.github.splotycode.mosaik.util.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public interface Parentable<I> {

    I getParent();

    default I getHeadParent() {
        I parent = getParent();
        while (parent instanceof Parentable && ((Parentable) parent).getParent() != null) {
            try {
                parent = (I) ((Parentable) parent).getParent();
            } catch (ClassCastException ex) {}
        }
        return parent;
    }

    default Collection<I> getAllParents() {
        if (getParent() == null) return Collections.emptyList();
        Collection<I> all = new ArrayList<>();
        I parent = getParent();
        while (parent != null) {
            all.add(parent);
            if (parent instanceof Parentable) {
                try {
                    parent = (I) ((Parentable) parent).getParent();
                } catch (ClassCastException ex) {}
            } else parent = null;
        }
        return all;
    }

}
