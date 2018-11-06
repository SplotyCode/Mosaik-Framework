package me.david.webapi.handler.anotation.transform.defaulttransformer;

import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.handler.anotation.transform.Transformer;
import me.david.webapi.server.Request;

import java.lang.reflect.Parameter;

public class RequestTransformer implements Transformer<Request> {

    @Override
    public boolean transformable(Parameter parameter) {
        return parameter.getType().equals(Request.class);
    }

    @Override
    public Request transform(Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) {
        return request;
    }
}
