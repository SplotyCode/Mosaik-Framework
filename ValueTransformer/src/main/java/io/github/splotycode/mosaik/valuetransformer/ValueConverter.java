package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.reflection.classregister.IListClassRegister;
import io.github.splotycode.mosaik.valuetransformer.impl.ValueConverterImpl;

import java.util.Collection;
import java.util.Optional;

public interface ValueConverter extends IListClassRegister<ValueTransformer> {
    ValueConverter DEFAULT = create().addCommonTransformers();

    static ValueConverter create() {
        return new ValueConverterImpl();
    }

    ValueConverter addCommonTransformers();

    <I, O> Optional<ConverterRoute<I, O>> route(Class<I> input, Class<O> result);
    <I, O> Optional<ConverterRoute<I, O>> route(Class<I> input, Class<O> result, Collection<ValueTransformer> transformers);
    <I, O> Optional<ConverterRoute<I, O>> routeWithAdditional(Class<I> input, Class<O> result, Collection<ValueTransformer> additional);
    <I, O> Optional<ConverterRoute<I, O>> routeWithOriginal(Class<I> input, Class<O> result, Collection<ValueTransformer> original);

    <T> Optional<ConverterRoute<T, String>> stringRoute(Class<T> input);
}
