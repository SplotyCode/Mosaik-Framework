package io.github.splotycode.mosaik.webapi.handler.anotation.parameter.defaultresolver;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.ParameterResolveException;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.AnnotatedParameterResolver;
import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.RequiredPost;

import java.lang.reflect.Parameter;

import static io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler.REQUEST;
import static io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler.SUP;

public class RequiredPostParameterResolver extends AnnotatedParameterResolver<RequiredPost, Object, AnnotationHttpHandler> {

    public RequiredPostParameterResolver() {
        super(RequiredPost.class);
    }

    @Override
    protected Object transformAnnotation(AnnotationHttpHandler context, RequiredPost annotation, Parameter parameter, DataFactory dataFactory) {
        String strValue = dataFactory.getData(REQUEST).getFirstPostParameter(annotation.value());
        if (strValue == null) throw new ParameterResolveException("Could not find " + annotation.value());

        Object value = context.parameterValue(dataFactory.getData(SUP), parameter, strValue);

        if (value == null) throw new ParameterResolveException("Invalid Data Type");
        return value;
    }
}
