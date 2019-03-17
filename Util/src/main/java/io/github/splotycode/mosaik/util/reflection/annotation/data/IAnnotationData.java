package io.github.splotycode.mosaik.util.reflection.annotation.data;

import java.lang.reflect.AnnotatedElement;

public interface IAnnotationData {

    Throwable getLoadError();
    void setLoadError(Throwable throwable);

    AnnotatedElement getElement();

    int getPriority();

    String getDisplayName();

}
