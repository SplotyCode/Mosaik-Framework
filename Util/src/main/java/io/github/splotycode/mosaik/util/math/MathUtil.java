package io.github.splotycode.mosaik.util.math;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * General Utils for math
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MathUtil {

    /**
     * Used for log operations were the base is not 10
     * @param base would be 10 in {@link Math#log10(double)}
     */
    public static double log(double base, double value) {
        return Math.log(value) / Math.log(base);
    }

    /**
     * Makes sure a value is in specific range
     * @param value the value you want to transform
     * @param min the minimum for value
     * @param max the maximum for value
     * @return the value that will be in the given range
     */
    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Makes sure a value is in specific range
     * @param value the value you want to transform
     * @param min the minimum for value
     * @param max the maximum for value
     * @return the value that will be in the given range
     */
    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Makes sure a value is in specific range
     * @param value the value you want to transform
     * @param min the minimum for value
     * @param max the maximum for value
     * @return the value that will be in the given range
     */
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

}
