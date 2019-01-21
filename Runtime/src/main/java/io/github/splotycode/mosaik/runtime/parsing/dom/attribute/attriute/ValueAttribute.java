package io.github.splotycode.mosaik.runtime.parsing.dom.attribute.attriute;

public abstract class ValueAttribute<T> extends Attribute {

    public ValueAttribute(String name) {
        super(name);
    }

    public abstract void setValue(T value);
    public abstract T getValue();
    public abstract String getStringValue();

}
