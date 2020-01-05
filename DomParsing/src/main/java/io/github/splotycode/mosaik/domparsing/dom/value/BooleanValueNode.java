package io.github.splotycode.mosaik.domparsing.dom.value;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BooleanValueNode implements ValueNode {

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
