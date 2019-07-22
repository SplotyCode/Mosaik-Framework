package io.github.splotycode.mosaik.util.reflection.annotation.method;

import io.github.splotycode.mosaik.util.reflection.annotation.AnnotationContext;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IAnnotationData;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface AnnotationHandler<M extends AnnotationContext, A extends Annotation, D extends IAnnotationData> {

    Class<? extends A> annotation();

    enum CallType {

        INIT,
        PRE,
        POST

    }

    default void doCall(AnnotatedElement element, CallType type, M context, D data) throws Exception {
        A annotation = element.getAnnotation(annotation());
        if (annotation != null) {
            switch (type) {
                case INIT:
                    init(context, annotation, data);
                    break;
                case PRE:
                    preCall(context, annotation, data);
                    break;
                case POST:
                    postCall(context, annotation, data);
                    break;
            }
        }
    }

    void init(M context, A annotation, D annotationData) throws Exception;
    void preCall(M context, A annotation, D annotationData) throws Exception;
    void postCall(M context, A annotation, D annotationData) throws Exception;

}
