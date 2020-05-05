package io.github.splotycode.mosaik.annotationbase.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

@AllArgsConstructor
@Getter
public abstract class PropertyAnnotationHandle<T extends Annotation> {

    private Class<T> propertyAnnotation;

    public T getAnnotation(AnnotatedElement element) {
        return element.getAnnotation(propertyAnnotation);
    }

    public String getName(AnnotatedElement element) {
        T annotation = getAnnotation(element);
        if (annotation != null) {
            return getName(annotation);
        }
        return "";
    }

    public abstract String getName(T annotation);

}
