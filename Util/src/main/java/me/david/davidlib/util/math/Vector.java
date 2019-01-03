package me.david.davidlib.util.math;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Vector {

    @Getter private final int size;
    @Getter protected final double[] values;

    public Vector(int size) {
        this.values = new double[size];
        this.size = size;
    }

    public Vector(double... v) {
        this.values = v;
        this.size = v.length;
    }

    public Vector add(Vector b) {
        checkEqualDimensions(b);

        Vector c = new Vector(size);
        for (int i = 0; i < size; i++)
            c.values[i] = values[i] + b.values[i];

        return c;
    }

    public Vector subtract(Vector b) {
        checkEqualDimensions(b);

        Vector c = new Vector(size);
        for (int i = 0; i < size; i++)
            c.values[i] = values[i] - b.values[i];

        return c;
    }

    public Vector multiply(Vector b) {
        checkEqualDimensions(b);

        Vector c = new Vector(size);
        for (int i = 0; i < size; i++)
            c.values[i] = values[i] * b.values[i];

        return c;
    }

    public Vector multiply(double s) {
        Vector c = new Vector(size);
        for (int i = 0; i < size; i++)
            c.values[i] = values[i] * s;

        return c;
    }

    public double dotVec(Vector b) {
        checkEqualDimensions(b);

        double s = 0;
        for (int i = 0; i < size; i++) {
            s += values[i] * b.values[i];
        }
        return s;
    }

    private void checkEqualDimensions(Vector b) {
        if (size != b.size) {
            throw new IllegalArgumentException("Wrong Vector Size");
        }
    }

    public double length() {
        return Math.sqrt(dotVec(this));
    }

    public double get(int i) {
        return values[i];
    }

    public double[] valueCopy() {
        double[] c = new double[size];
        System.arraycopy(values, 0, c, 0, size);
        return c;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (double val : values) {
            result.append(val).append(" ");
        }
        return result.toString();
    }
}
