package me.david.webapi.handler.anotation.parameter.defaultresolver;

import me.david.davidlib.link.LinkBase;
import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.handler.anotation.handle.UrlParam;
import me.david.webapi.handler.anotation.parameter.AnnotatedParameterResolver;
import me.david.webapi.handler.anotation.parameter.TransformerException;
import me.david.webapi.request.Request;

import java.lang.reflect.Parameter;

public class UrlParamParameterResolver extends AnnotatedParameterResolver<UrlParam, Object> {

    public UrlParamParameterResolver() {
        super(UrlParam.class);
    }

    @Override
    protected Object transformAnnotation(UrlParam annotation, Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) {
        if (handler.getMapping() == null && method.getMapping() == null) throw new TransformerException("Could not find mapping");

        String methodResult = method.getMapping().match(request.getPath()).getVariables().get(annotation.value());
        if (methodResult == null) {
            if (annotation.needed()) throw new TransformerException("Needed Parameter not present");
            return null;
        }

        Object value = LinkBase.getTransformerManager().transform(methodResult, parameter.getType());

        if (value == null) throw new TransformerException("Invalid Data Type");
        return value;
    }
}
