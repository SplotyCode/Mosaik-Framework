package io.github.splotycode.mosaik.util.io.resource;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NativeFormat {
    CHARS(false, true),
    BYTES(true, false),
    BOTH(true, true);

    public static NativeFormat select(boolean bytes, boolean chars) {
        if (bytes && chars) {
            return BOTH;
        }
        if (chars) {
            return CHARS;
        }
        return BYTES;
    }

    private final boolean bytes, chars;

    public boolean hasBytes() {
        return bytes;
    }

    public boolean hasChars() {
        return chars;
    }
}
