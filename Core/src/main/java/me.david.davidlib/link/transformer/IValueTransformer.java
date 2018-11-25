package me.david.davidlib.link.transformer;

import java.lang.reflect.ParameterizedType;

public interface IValueTransformer<I, O> {

    O transform(I input) throws TransformException;

    default Class<I> getInputClass() {
        return (Class<I>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    default Class<I> getOutputClass() {
        return (Class<I>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    default boolean valid(I input, Class<? extends O> output) {
        return getInputClass().isAssignableFrom(input.getClass()) && getOutputClass().isAssignableFrom(output);
    }

}
