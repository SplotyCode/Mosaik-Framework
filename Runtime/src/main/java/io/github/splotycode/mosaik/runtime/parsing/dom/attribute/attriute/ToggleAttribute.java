package io.github.splotycode.mosaik.runtime.parsing.dom.attribute.attriute;

import io.github.splotycode.mosaik.runtime.parsing.DomParseException;

public class ToggleAttribute extends ValueAttribute<Boolean> {

    private boolean value;
    private String rawValue;

    public ToggleAttribute(String name, String rawValue) {
        super(name);
        this.rawValue = rawValue;
        rawValue = rawValue.toLowerCase();
        switch (rawValue) {
            case "on":
                value = true;
                break;
            case "off":
                value = false;
                break;
            default:
                throw new DomParseException("Invalid Toggle value: '" + rawValue + "'");
        }
    }

    public ToggleAttribute(String name, Boolean value){
        super(name);
        this.value = value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return rawValue;
    }
}
