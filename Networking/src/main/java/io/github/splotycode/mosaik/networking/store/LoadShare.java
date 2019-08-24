package io.github.splotycode.mosaik.networking.store;

import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.math.MathUtil;
import lombok.Getter;

@Getter
public class LoadShare {

    private int min, max;
    private float[] weights;

    /* Max change for a weight per update */
    private float maxChange;
    private boolean maxChangePercent;

    private float sluggish;

    /*
     * When value ranges are over after update because of maxChange
     * should the overflow be distributed by their part?
     */
    private boolean overFlowPercent;

    public LoadShare(int min, int max, int size,
                     float maxChange, boolean maxChangePercent,
                     float sluggish, boolean overFlowPercent) {
        this(min, max, maxChange, maxChangePercent, sluggish, overFlowPercent);
        loadDefaults(size);
    }

    public LoadShare(int min, int max, float[] weights,
                     float maxChange, boolean maxChangePercent,
                     float sluggish, boolean overFlowPercent) {
        this(min, max, maxChange, maxChangePercent, sluggish, overFlowPercent);
        this.weights = weights;
    }

    protected LoadShare(int min, int max,
                        float maxChange, boolean maxChangePercent,
                        float sluggish, boolean overFlowPercent) {
        this.min = min;
        this.max = max;
        this.maxChange = maxChange;
        this.maxChangePercent = maxChangePercent;
        this.sluggish = sluggish;
        this.overFlowPercent = overFlowPercent;
        checkConfiguration();
    }

    protected void checkConfiguration() {
        if (maxChange != 0 && sluggish != 1 && maxChangePercent) {
            throw new IllegalArgumentException("Invalid configuration");
        }
    }

    public LoadShare(int min, int max, float[] weights) {
        this.min = min;
        this.max = max;
        sluggish = 1;
        this.weights = weights;
    }

    public LoadShare(int min, int max, int size) {
        this.min = min;
        this.max = max;
        sluggish = 1;
        loadDefaults(size);
    }

    public void resize(int newSize) {
        float[] newWeighs = new float[newSize];
        float diff = newSize / (float) weights.length;
        int i = 0;
        for (; i < Math.min(newSize, weights.length); i++) {
            newWeighs[i] = (weights[i] - min) / diff + min;
        }
        if (i != newSize) {
            float current = newWeighs[i - 1];
            float defaultPart = (max - (current + (max - weights[i - 1]) / diff)) / (newSize - i);
            for (; i < newSize; i++) {
                newWeighs[i] = (current += defaultPart);
            }
        }
        weights = newWeighs;
    }

    protected void loadDefaults(int size) {
        weights = new float[size];
        float percent = (max - min) / size;
        for (int i = 0; i < size; i++) {
            weights[i] = percent * i + min;
        }
    }

    public void update(long[] hits) {
        update(hits, ArrayUtil.sum(hits));
    }

    private float calcMaxChange(float current) {
        if (maxChangePercent) {
            return current * maxChange;
        }
        return maxChange;
    }

    public void update(long[] hits, long total) {
        int length = hits.length;
        if (length != weights.length) {
            throw new IllegalArgumentException("Statistic size match does not equal Share size");
        }

        int range = max - min;
        float last = 0;
        float overFlow = 0;
        for (int i = 0; i < length; i++) {
            float current = weights[i];
            float newValue = last + min;
            if (sluggish != 1) {
                float groundCurrent = current - min;
                newValue = (sluggish * (last - groundCurrent)) + groundCurrent + min;
            }
            if (maxChange != 0) {
                float beforeClamp = newValue;
                float maxChange = calcMaxChange(current);
                newValue = MathUtil.clamp(newValue, current - maxChange, current + maxChange);
                overFlow += newValue - beforeClamp;
            }
            weights[i] = newValue;
            last += (hits[i] / (float) total) * range;
        }

        if (overFlowPercent) {
            for (int i = 0; i < length; i++) {
                weights[i] = ((weights[i] - min) / range * overFlow);
            }
        } else {
            overFlow /= length;
            for (int i = 0; i < length; i++) {
                weights[i] += overFlow;
            }
        }
    }

}
