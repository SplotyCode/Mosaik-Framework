package io.github.splotycode.mosaik.valuetransformer.impl;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.ConverterRoute;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractRoute<I, O> implements ConverterRoute<I, O> {
    protected final Class resultType;

    protected <L_I, L_O> L_O doTransform(ValueTransformer<L_I, L_O> transformer, L_I input,
                                 Class<? extends L_O> resultType, DataFactory info) {
        try {
            return transformer.transform(input, resultType, info);
        } catch (Throwable throwable) {
            throw new TransformException("Failed to transform with " + transformer.getClass().getName(), throwable);
        }
    }
}
