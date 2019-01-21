package io.github.splotycode.mosaik.webapi.handler.anotation.parameter;

import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHandlerData;

import java.lang.reflect.Parameter;

public interface ParameterResolver<R> {

    boolean transformable(Parameter parameter);

    R transform(Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) throws ParameterResolveException;

}
