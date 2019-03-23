package io.github.splotycode.mosaik.webapi.handler.anotation.parameter.defaultresolver;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler;
import io.github.splotycode.mosaik.webapi.response.Response;

import java.lang.reflect.Parameter;

import static io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler.REQUEST;

public class ResponseParameterResolver implements ParameterResolver<Response, AnnotationHttpHandler> {

    @Override
    public boolean transformable(AnnotationHttpHandler context, Parameter parameter) {
        return parameter.getType().equals(Response.class);
    }

    @Override
    public Response transform(AnnotationHttpHandler context, Parameter parameter, DataFactory dataFactory) {
        return dataFactory.getData(REQUEST).getResponse();
    }

}
