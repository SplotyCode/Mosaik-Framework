package io.github.splotycode.mosaik.webapi.handler.anotation.parameter.defaultresolver;

import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHandlerData;
import io.github.splotycode.mosaik.webapi.handler.anotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.Response;

import java.lang.reflect.Parameter;

public class ResponseParameterResolver implements ParameterResolver<Response> {

    @Override
    public boolean transformable(Parameter parameter) {
        return parameter.getType().equals(Response.class);
    }

    @Override
    public Response transform(Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) {
        return request.getResponse();
    }

}
