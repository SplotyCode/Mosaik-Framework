package io.github.splotycode.mosaik.util.ml;

public interface Activation {

    Activation LINEAR = x -> x;

    Activation SIGMOID = x -> 1 / (1 + Math.pow(Math.E, -x));

    Activation RELU = x -> Math.max(0, x);

    /**
     * A.k.k hyperbolic tangent
     */
    Activation TANH = Math::tanh;

    Activation BINARY_STEP = step(1);

    static Activation step(double threshold) {
        return x -> x > threshold ? 1 : 0;
    }

    double activate(double x);

}
