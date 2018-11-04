package me.david.webapi.handler.anotation.transform.defaulttransformer;

import me.david.webapi.handler.anotation.handle.Get;
import me.david.webapi.handler.anotation.handle.RequiredGet;
import me.david.webapi.handler.anotation.transform.AnnotatedTransformer;
import me.david.webapi.handler.anotation.transform.TransformerException;
import me.david.webapi.server.Request;

import java.lang.reflect.Parameter;

public class RequiredGetTransformer extends AnnotatedTransformer<RequiredGet, String> {

    public RequiredGetTransformer() {
        super(RequiredGet.class);
    }

    @Override
    protected String transformAnnotation(RequiredGet annotation, Parameter parameter, Request request) {
        String result = request.getGet().get(annotation.value());
        if (result == null) throw new TransformerException("Could not find " + annotation.value());
        return result;
    }
}
