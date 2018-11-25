package me.david.webapi.handler.anotation.parameter.defaultresolver;

import me.david.davidlib.link.LinkBase;
import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.handler.anotation.handle.RequiredGet;
import me.david.webapi.handler.anotation.parameter.AnnotatedParameterResolver;
import me.david.webapi.handler.anotation.parameter.TransformerException;
import me.david.webapi.request.Request;

import java.lang.reflect.Parameter;

public class RequiredGetParameterResolver extends AnnotatedParameterResolver<RequiredGet, Object> {

    public RequiredGetParameterResolver() {
        super(RequiredGet.class);
    }

    @Override
    protected Object transformAnnotation(RequiredGet annotation, Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) {
        String strValue = request.getFirstGetParameter(annotation.value());
        if (strValue == null) throw new TransformerException("Could not find " + annotation.value());

        Object value = LinkBase.getTransformerManager().transform(strValue, parameter.getType());

        if (value == null) throw new TransformerException("Invalid Data Type");
        return value;
    }
}
