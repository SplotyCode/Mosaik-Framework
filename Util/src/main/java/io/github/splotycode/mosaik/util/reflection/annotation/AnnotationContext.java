package io.github.splotycode.mosaik.util.reflection.annotation;

import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface AnnotationContext<M extends AnnotationContext, D extends AnnotationData, T> {

    Class getClazz();
    Object getObject();
    AnnotationData globalData();

    void feed(T input);

    M self();

    Collection<AnnotationHandler<M, Annotation, D>> getAnnotationHandlers();

}
