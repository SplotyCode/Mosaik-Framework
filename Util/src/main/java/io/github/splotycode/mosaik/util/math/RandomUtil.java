package io.github.splotycode.mosaik.util.math;

import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomUtil {

    /**
     * Returns a random number
     * @param values the possible numbers
     * @param probability probability for each value
     */
    public static double weightedRandom(double[] values, double[] probability) {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(probability, "probability");
        if (values.length != probability.length) throw new IllegalArgumentException(values.length + "!=" + probability.length);

        double rand = Math.random();
        double ratio = 1f / ArrayUtil.sum(probability);
        double tempDist = 0;
        for (int c = 0; c < values.length;c++) {
            tempDist += probability[c];
            if (rand / ratio <= tempDist) {
                return values[c];
            }
        }
        return 0;
    }

    public static int fastRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static long fastRandom(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    public static double fastRandom(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max + 1);
    }

}
