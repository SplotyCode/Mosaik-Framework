package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import io.github.splotycode.mosaik.util.reflection.classregister.IListClassRegister;

import java.util.*;

public class TransformerManager implements IListClassRegister<ValueTransformer> {

    public static final DataKey<Class> RESULT = new DataKey<>("result");
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
        if (ReflectionUtil.isAssignable(result, input.getClass())) return (T) input;
        //List<Class<?>> possibleResults = getPossibleResults(input.getClass());

        for (ValueTransformer transformer : transformers) {
            if (transformer.valid(input, result)) {
                try {
                    DataFactory info = new DataFactory();
                    info.putData(RESULT, result);
                    return (T) transformer.transform(input, info);
                } catch (Throwable throwable) {
                    throw new TransformException("Failed to transform with " + transformer.getClass().getName(), throwable);
                }
            }
        }
        if (String.class.isAssignableFrom(result)) {
            return (T) input.toString();
        }
        return null;
    }

    public <T> T transformWithAdditional(Object input, Class<T> result, Collection<ValueTransformer> additional) {
        ArrayList<ValueTransformer> currentTransformers = new ArrayList<>(additional);
        currentTransformers.addAll(transformers);
        return transform(input, result, currentTransformers);
    }


    public <T> T transform(Object input, Class<T> result) {
        return transform(input, result, transformers);
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
