package me.david.webapi.handler.anotation.parameter.defaultresolver;

import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.handler.anotation.handle.HandleHelper;
import me.david.webapi.handler.anotation.handle.UrlParam;
import me.david.webapi.handler.anotation.parameter.AnnotatedParameterResolver;
import me.david.webapi.handler.anotation.parameter.ParameterResolveException;
import me.david.webapi.request.Request;

import java.lang.reflect.Parameter;

public class UrlParamParameterResolver extends AnnotatedParameterResolver<UrlParam, Object> {

    public UrlParamParameterResolver() {
        super(UrlParam.class);
    }

    @Override
    protected Object transformAnnotation(UrlParam annotation, Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) {
        if (handler.getMapping() == null && method.getMapping() == null) throw new ParameterResolveException("Could not find mapping");

        String methodResult = method.getMapping().match(request.getPath()).getVariables().get(annotation.value());
        if (methodResult == null) {
            if (annotation.needed()) throw new ParameterResolveException("Needed Parameter not present");
            return null;
        }

        Object value = HandleHelper.transformParameter(parameter, methodResult);

        if (value == null) throw new ParameterResolveException("Invalid Data Type");
        return value;
    }
}
