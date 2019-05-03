package io.github.splotycode.mosaik.util.math;

import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * General Utils for math
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MathUtil {

    public double weightedRandomn(double[] values, double[] probability) {
        if (values == null) throw new IllegalArgumentException("values");
        if (probability == null) throw new IllegalArgumentException("probability");
        if (values.length != probability.length) throw new IllegalArgumentException(values.length + "!=" + probability.length);

        double rand = Math.random();
        double ratio = 1.0f / ArrayUtil.sum(probability);
        double tempDist = 0;
        for (int c = 0; c < values.length;c++) {
            tempDist += probability[c];
            if (rand / ratio <= tempDist) {
                return values[c];
            }
        }
        return 0;
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
