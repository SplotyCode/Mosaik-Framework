package io.github.splotycode.mosaik.webapi.handler.anotation.parameter.defaultresolver;

import io.github.splotycode.mosaik.webapi.handler.anotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHandlerData;

import java.lang.reflect.Parameter;

public class RequestParameterResolver implements ParameterResolver<Request> {

    @Override
    public boolean transformable(Parameter parameter) {
        return parameter.getType().equals(Request.class);
    }

    @Override
    public Request transform(Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) {
        return request;
    }
}
