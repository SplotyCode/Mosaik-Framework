package io.github.splotycode.mosaik.annotationbase.context.parameter;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.annotationbase.context.AnnotationContext;

import java.lang.reflect.Parameter;

public class DataParameterResolver extends AnnotatedParameterResolver<Data, Object, AnnotationContext> {

    @Override
    protected Object transformAnnotation(AnnotationContext context, Data annotation, Parameter parameter, DataFactory dataFactory) {
        return dataFactory.getData(annotation.value(), null);
    }

}
