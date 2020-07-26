package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public interface ConverterRoute<I, O> {
    ConverterRoute DIRECT = new ConverterRoute() {
        @Override
        public Object convert(Object input) {
            return input;
        }

        @Override
        public Object convert(DataFactory info, Object input) {
            return convert(input);
        }
    };

    static <I, O> ConverterRoute<I, O> direct() {
        //noinspection unchecked
        return DIRECT;
    }

    O convert(I input);
    O convert(DataFactory info, I input);
}
