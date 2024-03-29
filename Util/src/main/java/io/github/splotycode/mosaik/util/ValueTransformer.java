package io.github.splotycode.mosaik.util;

import io.github.splotycode.mosaik.util.datafactory.DataFactories;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import lombok.Getter;

import java.lang.reflect.Type;

@Getter
public abstract class ValueTransformer<I, O> {
    private Class<I> inputClass;
    private Class<O> outputClass;

    /**
     * Transforms a object
     * @param input the original object
     * @return the transformed object
     */
    public O transform(I input) throws Exception {
        return transform(input, null, DataFactories.EMPTY_DATA_FACTORY);
    }

    /**
     * Transforms a object
     * @param input the original object
     * @param result the desired type of the result
     * @param info additionally transforming info
     * @return the transformed object
     */
    public abstract O transform(I input, Class<? extends O> result, DataFactory info) throws Exception;

    public ValueTransformer() {
        Type[] generics = ReflectionUtil.getGenerics(getClass());
        inputClass = (Class<I>) generics[0];
        outputClass = (Class<O>) generics[1];
    }

    /**
     * Checks if this transformer is able to make a specific transform operation
     * @param input the input object
     * @param output the class that the input should have after its transformation
     * @return true if it is able to transform or else false
     */
    public boolean valid(I input, Class<? extends O> output) {
        return ReflectionUtil.isAssignable(inputClass, input.getClass()) && ReflectionUtil.isAssignable(outputClass, output);
    }

    public boolean validInputType(Class<? extends I> input) {
        return ReflectionUtil.isAssignable(inputClass, input);
    }

    public boolean validOutputType(Class<? extends O> output) {
        return ReflectionUtil.isAssignable(outputClass, output);
    }
}
