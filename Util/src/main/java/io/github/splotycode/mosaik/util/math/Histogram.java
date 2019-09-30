package io.github.splotycode.mosaik.util.math;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.LongAdder;

public class Histogram {

    private LongAdder count = new LongAdder();
    private LongAdder sum = new LongAdder();
    private TreeMap<Long, LongAdder> buckets = new TreeMap<>();
    private boolean averageSupport = true;

    public Histogram(long... buckets) {
        for (long bukkit : buckets) {
            this.buckets.put(bukkit, new LongAdder());
        }
    }

    public Histogram(boolean averageSupport, long... buckets) {
        this.averageSupport = averageSupport;
        for (long bukkit : buckets) {
            this.buckets.put(bukkit, new LongAdder());
        }
    }

    public long min() {
        Map.Entry<Long, LongAdder> current = buckets.firstEntry();
        while (current != null) {
            if (current.getValue().sum() != 0) {
                return current.getKey();
            }
            current = buckets.higherEntry(current.getKey());
        }
        return Long.MAX_VALUE;
    }

    public long max() {
        Map.Entry<Long, LongAdder> current = buckets.lastEntry();
        while (current != null) {
            if (current.getValue().sum() != 0) {
                return current.getKey();
            }
            current = buckets.lowerEntry(current.getKey());
        }
        return Long.MIN_VALUE;
    }

    public long getAverage() {
        long count = this.count.sum();
        if (count == 0) {
            return 0;
        }
        return sum.sum() / count;
    }

    public double getMedian() {
        return getQuartile(50);
    }

    public double getQuartile(double quartile) {
        long needed = (long) (quartile / 100 * count.sum());
        long lastSum = 0;
        long currentSum = 0;
        Map.Entry<Long, LongAdder> previus = null;
        Map.Entry<Long, LongAdder> current = buckets.firstEntry();
        while (needed > currentSum && current != null) {
            lastSum = currentSum;
            currentSum += current.getValue().sum();
            previus = current;
            current = buckets.higherEntry(current.getKey());
        }
        if (previus == null) {
            if (current == null) {
                return Long.MIN_VALUE;
            }
            return current.getValue().sum();
        }
        if (current == null) {
            return previus.getValue().sum();
        }
        long firstOption = previus.getValue().sum();
        long lastOption = current.getValue().sum();
        return firstOption + (lastOption - firstOption) * (needed - firstOption) / (double) (currentSum - lastSum);
    }

    public void update(long value) {
        count.increment();
        if (averageSupport) {
            sum.add(value);
        }
        buckets.floorEntry(value).getValue().increment();
    }

    public long getSize() {
        return count.sum();
    }

}
