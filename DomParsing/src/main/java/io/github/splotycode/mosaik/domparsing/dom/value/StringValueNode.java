package io.github.splotycode.mosaik.domparsing.dom.value;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StringValueNode implements ValueNode {

    private String value;

    @Override
    public String name() {
        return value;
    }

    @Override
    public Object getRawValue() {
        return value;
    }
}
