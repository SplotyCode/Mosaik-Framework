package io.github.splotycode.mosaik.util.ml;

import java.util.Random;

public interface Filler {

    Random random = new Random();

    Filler ZERO = network -> 0;
    Filler STANDART_NORMAL = network -> random.nextGaussian();
    Filler STANDART_NORMAL_INPUT = network -> random.nextGaussian() / Math.pow(network.getInputSize(), 0.5);

    static Filler constant(double constant) {
        return network -> constant;
    }

    double fill(NeuralNetwork network);

}
