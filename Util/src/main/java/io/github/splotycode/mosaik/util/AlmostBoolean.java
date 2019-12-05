package io.github.splotycode.mosaik.util;

/**
 * Like a boolean but with a third state.
 * This third state can be used for many think,
 * one usage count be to use {@link AlmostBoolean#MAYBE} as not initialed.
 */
public enum AlmostBoolean {

    YES,
    NO,
    MAYBE;

    /**
     * Converts a boolean to AlmostBoolean
     * @return {@link AlmostBoolean#YES} if true or else {@link AlmostBoolean#NO}
     */
    public static AlmostBoolean fromBoolean(boolean value) {
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

    /**
     * Converts a AlmostBoolean to boolean
     * @param optimistically should {@link AlmostBoolean#MAYBE} be true or false
     */
    public boolean decide(boolean optimistically) {
        return optimistically ? this != NO : this == YES;
    }

}