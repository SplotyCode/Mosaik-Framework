package io.github.splotycode.mosaik.annotationbase.context.data;

import io.github.splotycode.mosaik.annotations.AnnotationHelper;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.annotationbase.context.parameter.ParameterResolver;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

public class AnnotationData implements IAnnotationData {

    @Getter @Setter protected Throwable loadError;
    @Getter @Setter protected AnnotatedElement element;

    @Getter @Setter protected String displayName;

    @Getter protected int priority;


    @Getter protected List<ParameterResolver> costomParameterResolvers = new ArrayList<>();
    @Getter protected List<ValueTransformer> costomTransformers = new ArrayList<>();

    @Override
    public void buildData(Annotation[] annotations) {
        priority = AnnotationHelper.getPriority(annotations);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
