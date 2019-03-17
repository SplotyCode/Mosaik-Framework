package io.github.splotycode.mosaik.util.reflection.annotation.data;

import io.github.splotycode.mosaik.annotations.AnnotationHelper;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
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

    public void buildData(Annotation[] annotations) {
        priority = AnnotationHelper.getPriority(annotations);
    }

}
