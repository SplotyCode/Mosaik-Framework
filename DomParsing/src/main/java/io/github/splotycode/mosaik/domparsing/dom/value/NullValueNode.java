package io.github.splotycode.mosaik.domparsing.dom.value;

public class NullValueNode implements ValueNode {

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public Object getRawValue() {
        return null;
    }
}
