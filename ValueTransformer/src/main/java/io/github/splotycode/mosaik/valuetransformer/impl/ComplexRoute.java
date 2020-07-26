package io.github.splotycode.mosaik.valuetransformer.impl;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.ConverterRoute;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

import java.util.Collection;
import java.util.LinkedList;

public class ComplexRoute<I, O> implements ConverterRoute<I, O> {
    public static Object safeNull(Class result) {
        if (result == int.class) {
            return 0;
        }
        if (result == long.class) {
            return 0L;
        }
        if (result == short.class) {
            return (short) 0;
        }

        if (result == float.class) {
            return 0F;
        }
        if (result == double.class) {
            return 0D;
        }

        if (result == boolean.class) {
            return false;
        }
        if (result == char.class) {
            return (char) 0;
        }
        return null;
    }

    private final Class resultType;
    private final Collection<ValueTransformer> transformers;

    public ComplexRoute(Class resultType, Collection<ValueTransformer> transformers) {
        this.resultType = resultType;
        this.transformers = new LinkedList<>(transformers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public O convert(I input) {
        Object result = input;
        for (ValueTransformer transformer : transformers) {
            try {
                result = transformer.transform(input, resultType, null);
            } catch (Throwable throwable) {
                throw new TransformException("Failed to transform with " + transformer.getClass().getName(), throwable);
            }
        }
        if (result == null) {
            /* Makes sure we never return null when the user requests a primitive type */
            return (O) safeNull(resultType);
        }
        return (O) result;
    }

    @Override
    public O convert(DataFactory info, I input) {
        return null;
    }
}
