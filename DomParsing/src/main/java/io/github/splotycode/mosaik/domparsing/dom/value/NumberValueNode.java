package io.github.splotycode.mosaik.domparsing.dom.value;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NumberValueNode implements ValueNode {

    private double value;

    @Override
    public String name() {
        return String.valueOf(value);
    }

    @Override
    public Object getRawValue() {
        return value;
    }
}
