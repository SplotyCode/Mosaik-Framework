package me.david.webapi.handler.anotation.transform;

import me.david.webapi.server.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public abstract class AnnotatedTransformer<A extends Annotation, R> implements Transformer<R> {

    private Class<? extends A> annotation;

    public AnnotatedTransformer(Class<? extends A> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean transformable(Parameter parameter) {
        return parameter.isAnnotationPresent(annotation);
    }

    @Override
    public final R transform(Parameter parameter, Request request) {
        return transformAnnotation(parameter.getAnnotation(annotation), parameter, request);
    }

    protected abstract R transformAnnotation(A annotation, Parameter parameter, Request request);

}
