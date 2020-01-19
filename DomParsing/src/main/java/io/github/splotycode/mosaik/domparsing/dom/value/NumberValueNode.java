package io.github.splotycode.mosaik.domparsing.dom.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NumberValueNode implements ValueNode {

    @Getter private double value;

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Object getRawValue() {
        return value;
    }
}
