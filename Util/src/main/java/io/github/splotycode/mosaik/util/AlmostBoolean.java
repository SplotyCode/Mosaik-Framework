package io.github.splotycode.mosaik.util;

public enum AlmostBoolean {

    YES,
    NO,
    MAYBE;

    public static AlmostBoolean fromBoolean(final boolean value) {
        return value ? YES : NO;
    }

    public boolean isYes() {
        return this == YES;
    }

    public boolean isNo() {
        return this == NO;
    }

    public boolean isMaybe() {
        return this == MAYBE;
    }

    public boolean decide(boolean optimistically) {
        return optimistically?this != NO:this == YES;
    }

}