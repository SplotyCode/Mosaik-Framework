package io.github.splotycode.mosaik.util.ml;

import io.github.splotycode.mosaik.util.math.Matrix;
import io.github.splotycode.mosaik.util.math.Vector;

public class NeuralNetwork {

    private Matrix weights;
    private Vector bias;
    private Activation activation;

    public int getInputSize() {
        return 0;
    }

    public double predict(double a) {
        for (int i = 0; i < bias.getSize(); i++) {
            a = activation.activate(weights.getColumn(i).dotVec(a) + bias.get(i));
        }
        return a;
    }

}
