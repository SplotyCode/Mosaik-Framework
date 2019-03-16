package io.github.splotycode.mosaik.webapi.handler.anotation.parameter.defaultresolver;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.AnnotatedParameterResolver;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.ParameterResolveException;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.HandleHelper;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.RequiredGet;

import java.lang.reflect.Parameter;

import static io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler.REQUEST;

public class RequiredGetParameterResolver extends AnnotatedParameterResolver<RequiredGet, Object> {

    public RequiredGetParameterResolver() {
        super(RequiredGet.class);
    }

    @Override
    protected Object transformAnnotation(RequiredGet annotation, Parameter parameter, DataFactory dataFactory) {
        String strValue = dataFactory.getData(REQUEST).getFirstGetParameter(annotation.value());
        if (strValue == null) throw new ParameterResolveException("Could not find " + annotation.value());

        Object value = HandleHelper.transformParameter(parameter, strValue);

        if (value == null) throw new ParameterResolveException("Invalid Data Type");
        return value;
    }
}
