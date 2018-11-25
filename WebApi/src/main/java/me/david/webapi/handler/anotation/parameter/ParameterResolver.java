package me.david.webapi.handler.anotation.parameter;

import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.request.Request;

import java.lang.reflect.Parameter;

public interface ParameterResolver<R> {

    boolean transformable(Parameter parameter);

    R transform(Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) throws TransformerException;

}
