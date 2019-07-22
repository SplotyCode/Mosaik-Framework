package io.github.splotycode.mosaik.util.reflection.annotation;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IAnnotationData;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collection;

public interface AnnotationContext<C extends AnnotationContext, D extends IAnnotationData, T> {

    Class getClazz();
    Object getObject();
    D data();

    Object parameterValue(D data, Parameter parameter, String input);

    default Object parameterValue(Parameter parameter, String input) {
        return parameterValue(data(), parameter, input);
    }

    Object rawTransform(String input, Class<?> result, Collection<ValueTransformer> transformers);

    void feed(T input);

    Object callmethod(D data, DataFactory additionalInfo);

    C self();

    Collection<AnnotationHandler<C, ? extends Annotation, D>> getAnnotationHandlers();

    Class<? extends D> elementClass();

}
