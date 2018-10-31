package me.david.webapi.handler.anotation.transform;

import me.david.webapi.server.Request;

import java.lang.reflect.Parameter;

public interface Transformer<R> {

    boolean transformable(Parameter parameter);

    R transform(Parameter parameter, Request request);

}
