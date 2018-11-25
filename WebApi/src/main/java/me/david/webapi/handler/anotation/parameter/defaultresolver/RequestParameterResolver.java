package me.david.webapi.handler.anotation.parameter.defaultresolver;

import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.handler.anotation.parameter.ParameterResolver;
import me.david.webapi.request.Request;

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
