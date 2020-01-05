package io.github.splotycode.mosaik.util.node;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode
public class DefaultNode<N extends DefaultNode<N>> implements Parentable<N>, Childable<N>, Node {

    @Getter
    protected N parent;

    @Getter
    protected Collection<N> childes;

    public DefaultNode() {
        this(null);
    }

    public DefaultNode(N parent) {
        this(parent, new ArrayList<>());
    }

    public DefaultNode(N parent, Collection<N> childes) {
        this.parent = parent;
        this.childes = childes;
    }
}
