package me.david.davidlib.link.transformer;

import lombok.Getter;
import me.david.davidlib.utils.reflection.ReflectionUtil;

import java.lang.reflect.Type;

public abstract class ValueTransformer<I, O> {

    @Getter private Class<I> inputClass;
    @Getter private Class<O> outputClass;

    public abstract O transform(I input) throws TransformException;

    public ValueTransformer() {
        Type[] generetics = ReflectionUtil.getGenerretics(getClass());
        inputClass = (Class<I>) generetics[0];
        outputClass = (Class<O>) generetics[1];
    }

    public boolean valid(I input, Class<? extends O> output) {
        return ReflectionUtil.isAssignable(inputClass, input.getClass()) && ReflectionUtil.isAssignable(outputClass, output);
    }

}
