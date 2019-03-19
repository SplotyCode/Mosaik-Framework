package io.github.splotycode.mosaik.util.reflection.annotation.method;

import io.github.splotycode.mosaik.util.reflection.annotation.AnnotationContext;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IAnnotationData;

import java.lang.annotation.Annotation;

public interface AnnotationHandler<M extends AnnotationContext, A extends Annotation, D extends IAnnotationData> {

    Class<? extends A> annotation();

    void init(M context, A annotation, D annotationData) throws Exception;
    void preCall(M context, A annotation, D annotationData) throws Exception;
    void postCall(M context, A annotation, D annotationData) throws Exception;

}
