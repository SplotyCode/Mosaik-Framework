package io.github.splotycode.mosaik.util.reflection.annotation;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.data.AnnotationData;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface AnnotationContext<M extends AnnotationContext, D extends AnnotationData, T> {

    Class getClazz();
    Object getObject();
    AnnotationData globalData();

    void feed(T input);

    Object callmethod(D data, DataFactory additionalInfo);

    M self();

    Collection<AnnotationHandler<M, Annotation, D>> getAnnotationHandlers();

}
