package io.github.splotycode.mosaik.util.reflection.annotation.parameter;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.AnnotationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public abstract class AnnotatedParameterResolver<A extends Annotation, R, C extends AnnotationContext> implements ParameterResolver<R, C> {

    protected Class<? extends A> annotation;

    public AnnotatedParameterResolver(Class<? extends A> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean transformable(C context, Parameter parameter) {
        return parameter.isAnnotationPresent(annotation);
    }

    @Override
    public final R transform(C context, Parameter parameter, DataFactory dataFactory) {
        return transformAnnotation(context, parameter.getAnnotation(annotation), parameter, dataFactory);
    }

    protected abstract R transformAnnotation(C context, A annotation, Parameter parameter, DataFactory dataFactory);

}
