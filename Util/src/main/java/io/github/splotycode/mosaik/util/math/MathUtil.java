package io.github.splotycode.mosaik.util.math;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * General Utils for math
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MathUtil {

    /**
     * Returns a random number
     * @param values the possible numbers
     * @param probability probability for each value
     * @deprecated {@link RandomUtil#weightedRandom(double[], double[])}
     */
    @Deprecated
    public static double weightedRandom(double[] values, double[] probability) {
        return RandomUtil.weightedRandom(values, probability);
    }

    /**
     * Reverse a Number.
     * Example: 2345 => 5432
     */
    public short reverse(short n) {
        short reverse = 0;
        while (n != 0) {
            reverse *= 10;
            reverse += n % 10;
            n /= 10;
        }
        return reverse;
    }

    /**
     * Reverse a Number.
     * Example: 2345 => 5432
     */
    public long reverse(long n) {
        long reverse = 0;
        while (n != 0) {
            reverse *= 10;
            reverse += n % 10;
            n /= 10;
        }
        return reverse;
    }

    /**
     * Reverse a Number.
     * Example: 2345 => 5432
     */
    public int reverse(int n) {
        int reverse = 0;
        while (n != 0) {
            reverse *= 10;
            reverse += n % 10;
            n /= 10;
        }
        return reverse;
    }

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
