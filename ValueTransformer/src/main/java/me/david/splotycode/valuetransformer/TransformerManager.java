package me.david.splotycode.valuetransformer;

import me.david.davidlib.runtime.transformer.ITransformerManager;
import me.david.davidlib.runtime.transformer.ValueTransformer;

import java.util.*;

public class TransformerManager implements ITransformerManager {

    private Set<ValueTransformer> transformers = new HashSet<>();

    @Override
    public Class<ValueTransformer> getObjectClass() {
        return ValueTransformer.class;
    }

    //TODO: if we have a transformer that transforms int to string and a transformer that transforms string to short it should be possible to transform int to shorts
    @Override
    public <T> T transform(Object input, Class<T> result) {
        if (result.isAssignableFrom(input.getClass())) return (T) input;
        //List<Class<?>> possibleResults = getPossibleResults(input.getClass());
        for (ValueTransformer transformer : transformers) {
            if (transformer.valid(input, result)) {
                return (T) transformer.transform(input);
            }
        }
        return null;
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
