package io.github.splotycode.mosaik.webapi.handler.anotation.parameter.defaultresolver;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.annotationbase.context.parameter.AnnotatedParameterResolver;
import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.Get;
import io.github.splotycode.mosaik.webapi.request.Request;

import java.lang.reflect.Parameter;

public class GetParameterResolver extends AnnotatedParameterResolver<Get, Object, AnnotationHttpHandler> {

    public GetParameterResolver() {
        super(Get.class);
    }

    @Override
    protected Object transformAnnotation(AnnotationHttpHandler context, Get annotation, Parameter parameter, DataFactory dataFactory) {
        Request request = dataFactory.getData(AnnotationHttpHandler.REQUEST);
        String strValue = request.getFirstGetParameter(annotation.value());
        return context.parameterValue(dataFactory.getData(AnnotationHttpHandler.SUP), parameter, strValue);
    }
}
