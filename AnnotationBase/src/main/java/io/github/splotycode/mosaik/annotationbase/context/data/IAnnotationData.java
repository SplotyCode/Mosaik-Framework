package io.github.splotycode.mosaik.annotationbase.context.data;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.annotationbase.context.parameter.ParameterResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

public interface IAnnotationData {

    void buildData(Annotation[] annotations);

    Throwable getLoadError();
    void setLoadError(Throwable throwable);

    AnnotatedElement getElement();
    void setElement(AnnotatedElement element);

    int getPriority();

    String getDisplayName();
    void setDisplayName(String name);

    List<ParameterResolver> getCostomParameterResolvers();

    List<ValueTransformer> getCostomTransformers();

}
