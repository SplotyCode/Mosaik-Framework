package io.github.splotycode.mosaik.domparsing.dom.value;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BooleanValueNode implements ValueNode {

    public static final BooleanValueNode TRUE = new BooleanValueNode(true);
    public static final BooleanValueNode FALSE = new BooleanValueNode(false);

    private boolean value;

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Object getRawValue() {
        return value;
    }
}
