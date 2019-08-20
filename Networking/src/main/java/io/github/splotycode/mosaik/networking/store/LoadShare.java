package io.github.splotycode.mosaik.networking.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoadShare {

    private int min, max;
    private float[] weights;

    public LoadShare(int min, int max, int size) {
        this.min = min;
        this.max = max;
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
        if (hits.length != weights.length) {
            throw new IllegalArgumentException("Statistic size match does not equal Share size");
        }
        double total = 0;
        for (long data : hits) {
            total += data - min;
        }
        int range = max - min;
        float last = 0;
        for (int i = 0; i < hits.length; i++) {
            float weight = last + ((float) (hits[i] / total) * range + min);
            weights[i] = weight;
            last = weight;
        }
    }

}
