package io.github.splotycode.mosaik.valuetransformer.impl;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;

public class DirectTransformerRoute<I, O> extends AbstractRoute<I, O> {
    private final ValueTransformer<I, O> transformer;

    public DirectTransformerRoute(Class resultType, ValueTransformer<I, O> transformer) {
        super(resultType);
        this.transformer = transformer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public O convert(DataFactory info, I input) {
        Object result = doTransform(transformer, input, resultType, info);
        if (result == null) {
            /* Makes sure we never return null when the user requests a primitive type */
            return (O) ReflectionUtil.primitiveSafeNull(resultType);
        }
        return (O) result;
    }
}
