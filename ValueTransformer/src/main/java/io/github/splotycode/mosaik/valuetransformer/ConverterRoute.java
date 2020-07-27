package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactories;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.impl.DirectTransformerRoute;

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

    static <I, O> ConverterRoute<I, O> fromTransformer(Class resultType, ValueTransformer<I, O> transformer) {
        return new DirectTransformerRoute<>(resultType, transformer);
    }

    default O convert(I input) {
        return convert(DataFactories.EMPTY_DATA_FACTORY, input);
    }

    O convert(DataFactory info, I input);
}
