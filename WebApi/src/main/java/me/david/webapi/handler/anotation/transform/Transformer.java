package me.david.webapi.handler.anotation.transform;

import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.request.Request;

import java.lang.reflect.Parameter;

public interface Transformer<R> {

    boolean transformable(Parameter parameter);

    R transform(Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) throws TransformerException;

}
