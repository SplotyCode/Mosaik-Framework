package me.david.webapi.handler.anotation.transform.defaulttransformer;

import me.david.webapi.handler.anotation.handle.Get;
import me.david.webapi.handler.anotation.transform.TransformerException;
import me.david.webapi.server.Request;

import java.lang.reflect.Parameter;

public class RequiredGetTransformer extends GetTransformer {

    @Override
    protected String transformAnnotation(Get annotation, Parameter parameter, Request request) {
        String result = super.transformAnnotation(annotation, parameter, request);
        if (result == null) throw new TransformerException("Could not find " + annotation.value());
        return result;
    }
}
