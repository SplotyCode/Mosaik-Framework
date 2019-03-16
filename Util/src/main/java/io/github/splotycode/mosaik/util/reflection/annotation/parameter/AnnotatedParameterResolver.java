package io.github.splotycode.mosaik.util.reflection.annotation.parameter;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public abstract class AnnotatedParameterResolver<A extends Annotation, R> implements ParameterResolver<R> {

    protected Class<? extends A> annotation;

    public AnnotatedParameterResolver(Class<? extends A> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean transformable(Parameter parameter) {
        return parameter.isAnnotationPresent(annotation);
    }

    @Override
    public final R transform(Parameter parameter, DataFactory dataFactory) {
        return transformAnnotation(parameter.getAnnotation(annotation), parameter, dataFactory);
    }

    protected abstract R transformAnnotation(A annotation, Parameter parameter, DataFactory dataFactory);

}
