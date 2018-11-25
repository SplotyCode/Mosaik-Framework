package me.david.webapi.handler.anotation.transform.defaulttransformer;

import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.handler.anotation.handle.UrlParam;
import me.david.webapi.handler.anotation.transform.AnnotatedTransformer;
import me.david.webapi.handler.anotation.transform.TransformerException;
import me.david.webapi.request.Request;

import java.lang.reflect.Parameter;

public class UrlParamTransformer extends AnnotatedTransformer<UrlParam, String> {

    public UrlParamTransformer() {
        super(UrlParam.class);
    }

    @Override
    protected String transformAnnotation(UrlParam annotation, Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) {
        if (handler.getMapping() == null && method.getMapping() == null) throw new TransformerException("Could not find mapping");
        String methodResult = method.getMapping().match(request.getPath()).getVariables().get(annotation.value());
        if (methodResult != null) {
            return methodResult;
        }

        return handler.getMapping().match(request.getPath()).getVariables().get(annotation.value());
    }
}
