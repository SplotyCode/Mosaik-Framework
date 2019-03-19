package io.github.splotycode.mosaik.util.reflection.annotation;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IAnnotationData;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface AnnotationContext<C extends AnnotationContext, D extends IAnnotationData, T> {

    Class getClazz();
    Object getObject();
    D data();

    void feed(T input);

    Object callmethod(D data, DataFactory additionalInfo);

    C self();

    Collection<AnnotationHandler<C, Annotation, D>> getAnnotationHandlers();

    Class<? extends D> elementClass();

}
