package io.github.splotycode.mosaik.domparsing.dom.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class StringValueNode implements ValueNode {

    @Getter private String value;

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Object getRawValue() {
        return value;
    }
}
