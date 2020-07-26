package io.github.splotycode.mosaik.valuetransformer.impl;

import io.github.splotycode.mosaik.runtime.Runtime;
import io.github.splotycode.mosaik.util.AlmostBoolean;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import io.github.splotycode.mosaik.util.reflection.collector.ClassCollector;
import io.github.splotycode.mosaik.valuetransformer.ConverterRoute;
import io.github.splotycode.mosaik.valuetransformer.ValueConverter;

import java.util.*;

public class ValueConverterImpl implements ValueConverter {
    public static final ClassCollector COMMON_COLLECTOR = ClassCollector.newInstance()
            .setAbstracation(AlmostBoolean.NO)
            .setInPackage("io.github.splotycode.mosaik.valuetransformer.common")
            .setNoDisable(true)
            .setNeedAssignable(ValueTransformer.class);

    private Set<ValueTransformer> transformers = new HashSet<>();

    @Override
    public Collection<ValueTransformer> getList() {
        return transformers;
    }

    @Override
    public ValueConverter addCommonTransformers() {
        registerAll(COMMON_COLLECTOR, Runtime.getRuntime().getGlobalClassPath());
        return this;
    }

    @Override
    public <I, O> Optional<ConverterRoute<I, O>> route(Class<I> input, Class<O> result) {
        return route(input, result, transformers);
    }

    @Override
    public <I, O> Optional<ConverterRoute<I, O>> route(Class<I> input, Class<O> result,
                                                       Collection<ValueTransformer> transformers) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(result, "result");

        if (ReflectionUtil.isAssignable(result, input)) {
            return Optional.of(ConverterRoute.direct());
        }

        Stack<ValueTransformer> stack = new Stack<>();
        if (findNext(transformers, stack, input, result)) {
            return Optional.of(new ComplexRoute<>(result, stack));
        }
        return Optional.empty();
    }

    private boolean findNext(Collection<ValueTransformer> transformers, Stack<ValueTransformer> stack, Class input, Class desired) {
        for (ValueTransformer transformer : transformers) {
            if (transformer.validInputType(input)) {
                stack.push(transformer);
                if (transformer.validOutputType(desired) ||
                        findNext(transformers, stack, transformer.getOutputClass(), desired)) {
                    return true;
                }
                stack.pop();
            }
        }
        return false;
    }

    @Override
    public <I, O> Optional<ConverterRoute<I, O>> routeWithAdditional(Class<I> input, Class<O> result, Collection<ValueTransformer> additional) {
        return route(input, result, CollectionUtil.mergeCollections(additional, transformers));
    }

    @Override
    public <T> Optional<ConverterRoute<T, String>> stringRoute(Class<T> input) {
        return route(input, String.class);
    }
}
