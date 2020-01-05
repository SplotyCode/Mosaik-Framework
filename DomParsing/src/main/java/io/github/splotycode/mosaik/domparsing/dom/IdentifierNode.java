package io.github.splotycode.mosaik.domparsing.dom;

import io.github.splotycode.mosaik.domparsing.dom.value.NullValueNode;
import io.github.splotycode.mosaik.domparsing.dom.value.ValueNode;
import io.github.splotycode.mosaik.util.node.Childable;
import io.github.splotycode.mosaik.util.node.NameableNode;

public interface IdentifierNode extends NameableNode, Childable<ValueNode> {

    default boolean hasOnlyNull() {
        for (ValueNode value : getChildes())  {
            if (!(value instanceof NullValueNode)) {
                return false;
            }
        }
        return true;
    }

    default boolean areAllOfType(Class<? extends ValueNode> type) {
        for (ValueNode value : getChildes()) {
            if (!type.isInstance(value)) {
                return false;
            }
        }
        return !getChildes().isEmpty();
    }

}
