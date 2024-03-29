package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactories;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import io.github.splotycode.mosaik.util.reflection.classregister.IListClassRegister;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformerNotFoundException;

import java.util.*;

@SuppressWarnings("WeakerAccess")
@Deprecated
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

    public <T> T transform(Object input, Class<T> result, Collection<ValueTransformer> transformers) {
        return transform(DataFactories.EMPTY_DATA_FACTORY, input, result, transformers);
    }

    public <T> T transform(DataFactory info, Object input, Class<T> result, Collection<ValueTransformer> transformers) {
        Objects.requireNonNull(info, "info");
        Objects.requireNonNull(result, "result");
        if (input == null) {
            //noinspection unchecked
            return (T) ReflectionUtil.primitiveSafeNull(result);
        }

        Class<?> inputClass = input.getClass();
        if (ReflectionUtil.isAssignable(result, inputClass)) {
            /* We can not ue inputClass.cast here */
            //noinspection unchecked
            return (T) input;
        }

        if (info == DataFactories.EMPTY_DATA_FACTORY) {
            info = DataFactories.singletonDataFactory(CommonData.RESULT, result);
        } else {
            info.putData(CommonData.RESULT, result);
        }

        ValueTransformer<Object, T> transformer = getTransformer(input, result, transformers);
        if (transformer != null) {
            try {
                return transformer.transform(input, result, info);
            } catch (Throwable throwable) {
                throw new TransformException("Failed to transform with " + transformer.getClass().getName(), throwable);
            }
        }
        if (String.class.isAssignableFrom(result)) {
            String str = input.toString();
            if (!info.getDataDefault(CommonData.AVOID_TOSTRING, false) ||
                    getTransformer(str, input.getClass(), transformers) != null) {
                //noinspection unchecked
                return (T) str;
            }
        }
        if (info.getDataDefault(CommonData.AVOID_NULL, false)) {
            throw new TransformerNotFoundException("No transformer found to convert " + inputClass.getName() + " to " + result.getName());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <I, O> ValueTransformer<I, O> getTransformer(I input, Class<O> result, Collection<ValueTransformer> transformers) {
        for (ValueTransformer transformer : transformers) {
            if (transformer.valid(input, result)) {
                return transformer;
            }
        }
        return null;
    }

    public <T> T transformWithAdditional(Object input, Class<T> result, Collection<ValueTransformer> additional) {
        return transformWithAdditional(DataFactories.EMPTY_DATA_FACTORY, input, result, additional);
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
        for (ValueTransformer<?, ?> transformer : transformers) {
            if (transformer.getInputClass().isAssignableFrom(input)) {
                list.addAll(getPossibleResults(transformer.getOutputClass()));
            }
        }
        return list;
    }

    public String transformToString(Object input) {
        return transform(input, String.class);
    }

    @Override
    public Collection<ValueTransformer> getList() {
        return transformers;
    }
}
