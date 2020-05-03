package io.github.splotycode.mosaik.webapi.handler.anotation.parameter.defaultresolver;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.annotationbase.context.parameter.ParameterResolver;
import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler;
import io.github.splotycode.mosaik.webapi.request.Request;

import java.lang.reflect.Parameter;

import static io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler.REQUEST;

public class RequestParameterResolver implements ParameterResolver<Request, AnnotationHttpHandler> {

    @Override
    public boolean transformable(AnnotationHttpHandler context, Parameter parameter) {
        return parameter.getType().equals(Request.class);
    }

    @Override
    public Request transform(AnnotationHttpHandler context, Parameter parameter, DataFactory dataFactory) {
        return dataFactory.getData(REQUEST);
    }
}
