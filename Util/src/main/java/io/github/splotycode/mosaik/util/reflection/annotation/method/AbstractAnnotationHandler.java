package io.github.splotycode.mosaik.util.reflection.annotation.method;

import io.github.splotycode.mosaik.util.reflection.annotation.AnnotationContext;
import io.github.splotycode.mosaik.util.reflection.annotation.AnnotationData;

import java.lang.annotation.Annotation;

public abstract class AbstractAnnotationHandler<M extends AnnotationContext, A extends Annotation, D extends AnnotationData> implements AnnotationHandler<M, A, D> {

    protected Class<? extends A> annotation;

    @Override
    public Class<? extends A> annotation() {
        return annotation;
    }

}
