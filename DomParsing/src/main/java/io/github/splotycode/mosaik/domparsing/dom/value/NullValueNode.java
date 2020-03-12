package io.github.splotycode.mosaik.domparsing.dom.value;

public class NullValueNode implements ValueNode {

    public static final NullValueNode INSTANCE = new NullValueNode();

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public Object getRawValue() {
        return null;
    }
}
