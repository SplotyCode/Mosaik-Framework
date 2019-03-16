package io.github.splotycode.mosaik.util.reflection.annotation;

import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AnnotationData {

    @Getter @Setter
    protected Throwable loadError;
    @Setter protected AnnotatedElement element;

    public void buildData(Annotation[] annotations) {

    }

}
