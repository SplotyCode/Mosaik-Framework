package me.david.davidlib.link.transformer;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;

public abstract class ValueTransformer<I, O> {

    @Getter private Class<I> inputClass;
    @Getter private Class<O> outputClass;

    public abstract O transform(I input) throws TransformException;

    public ValueTransformer() {
        inputClass = (Class<I>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        outputClass = (Class<O>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public boolean valid(I input, Class<? extends O> output) {
        return assisnable(inputClass, input.getClass()) && assisnable(outputClass, output);
    }

    private boolean assisnable(Class one, Class two) {
        return one.isAssignableFrom(two) || comparePrimi(one, two);
    }

    private boolean comparePrimi(Class one, Class two) {
        boolean result =  samePrimitive(one, two, "int", "Integer") ||
               samePrimitive(one, two, "float", "Float") ||
               samePrimitive(one, two, "double", "Double") ||
               samePrimitive(one, two, "char", "Character") ||
               samePrimitive(one, two, "long", "Long") ||
               samePrimitive(one, two, "short", "Short");
        return result;
    }

    private boolean samePrimitive(Class one, Class two, String primiClass, String clazz) {
        String oneName = one.getSimpleName(),
               twoName = two.getSimpleName();
        if (one.isPrimitive() && oneName.equalsIgnoreCase(primiClass) && twoName.equalsIgnoreCase(clazz)) return two.isArray() == one.isArray();
        if (two.isPrimitive() && twoName.equalsIgnoreCase(primiClass) && oneName.equalsIgnoreCase(clazz)) return two.isArray() == one.isArray();
        return false;
    }

}
