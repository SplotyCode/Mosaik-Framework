package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import io.github.splotycode.mosaik.util.reflection.classregister.IListClassRegister;

import java.util.*;

public class TransformerManager implements IListClassRegister<ValueTransformer> {

    public static final DataKey<TransformerManager> LINK = new DataKey<>("transformer.manager");

    public static TransformerManager getInstance() {
        return LinkBase.getInstance().getLink(LINK);
    }

    private Set<ValueTransformer> transformers = new HashSet<>();

    @Override
    public Class<ValueTransformer> getObjectClass() {
        return ValueTransformer.class;
    }

    //TODO: if we have a transformer that transforms int to string and a transformer that transforms string to short it should be possible to transform int to shorts

    public <T> T transform(Object input, Class<T> result, Collection<ValueTransformer> transformers) {
        return transform(new DataFactory(), input, result, transformers);
    }

    public <T> T transform(DataFactory info, Object input, Class<T> result, Collection<ValueTransformer> transformers) {
        Objects.requireNonNull(info, "info");
        if (input == null) return null;

        Class<?> inputClass = input.getClass();
        if (ReflectionUtil.isAssignable(result, inputClass)) return (T) input;

        info.putData(CommonData.RESULT, result);

        ValueTransformer<Object, T> transformer = getTransformer(input, result, transformers);
        if (transformer != null) {
            try {
                return transformer.transform(input, info);
            } catch (Throwable throwable) {
                throw new TransformException("Failed to transform with " + transformer.getClass().getName(), throwable);
            }
        }
        if (String.class.isAssignableFrom(result)) {
            String str = input.toString();
            if (!info.getDataDefault(CommonData.AVOID_TOSTRING, false) ||
                    getTransformer(str, input.getClass(), transformers) != null) {
                return (T) str;
            }
        }
        if (info.getDataDefault(CommonData.AVOID_NULL, false)) {
            throw new TransformerNotFoundException("No transformer found to convert " + inputClass.getName() + " to " + result.getName());
        }
        return null;
    }

    private <I, O> ValueTransformer<I, O> getTransformer(I input, Class<O> result, Collection<ValueTransformer> transformers) {
        for (ValueTransformer transformer : transformers) {
            if (transformer.valid(input, result)) {
                return transformer;
            }
        }
        return null;
    }

    public <T> T transformWithAdditional(Object input, Class<T> result, Collection<ValueTransformer> additional) {
        return transformWithAdditional(new DataFactory(), input, result, additional);
    }

    public <T> T transformWithAdditional(DataFactory info, Object input, Class<T> result, Collection<ValueTransformer> additional) {
        /* Use additional first as mergeCollections will append and not override */
        List<ValueTransformer> currentTransformers = CollectionUtil.mergeCollections(additional, transformers);
        return transform(info, input, result, currentTransformers);
    }


    public <T> T transform(Object input, Class<T> result) {
        return transform(input, result, transformers);
    }

    public <T> T transform(DataFactory info, Object input, Class<T> result) {
        return transform(info, input, result, transformers);
    }

    private List<Class<?>> getPossibleResults(Class<?> input) {
        List<Class<?>> list = new ArrayList<>();
        list.add(input);
        for (ValueTransformer transformer : transformers) {
            if (transformer.getInputClass().isAssignableFrom(input)) {
                list.addAll(getPossibleResults(transformer.getOutputClass()));
            }
        }
        return list;
    }

    @Override
    public Collection<ValueTransformer> getList() {
        return transformers;
    }
}
