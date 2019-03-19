package io.github.splotycode.mosaik.util.reflection.annotation.data;

import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;

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

}
