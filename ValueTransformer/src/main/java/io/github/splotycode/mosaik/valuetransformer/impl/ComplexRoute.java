package io.github.splotycode.mosaik.valuetransformer.impl;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;

import java.util.Collection;
import java.util.LinkedList;

public class ComplexRoute<I, O> extends AbstractRoute<I, O> {
    private final Collection<ValueTransformer> transformers;

    public ComplexRoute(Class resultType, Collection<ValueTransformer> transformers) {
        super(resultType);
        this.transformers = new LinkedList<>(transformers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public O convert(DataFactory info, I input) {
        Object result = input;
        for (ValueTransformer transformer : transformers) {
            result = doTransform(transformer, result, resultType, info);
        }
        if (result == null) {
            /* Makes sure we never return null when the user requests a primitive type */
            return (O) ReflectionUtil.primitiveSafeNull(resultType);
        }
        return (O) result;
    }
}
