package me.david.webapi.handler.anotation.transform;

import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.server.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public abstract class AnnotatedTransformer<A extends Annotation, R> implements Transformer<R> {

    protected Class<? extends A> annotation;

    public AnnotatedTransformer(Class<? extends A> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean transformable(Parameter parameter) {
        return parameter.isAnnotationPresent(annotation);
    }

    @Override
    public final R transform(Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) {
        return transformAnnotation(parameter.getAnnotation(annotation), parameter, request, handler, method);
    }

    protected abstract R transformAnnotation(A annotation, Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method);

}
