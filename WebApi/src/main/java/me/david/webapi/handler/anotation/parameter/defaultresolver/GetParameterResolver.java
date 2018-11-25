package me.david.webapi.handler.anotation.parameter.defaultresolver;

import me.david.davidlib.link.LinkBase;
import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.handler.anotation.handle.Get;
import me.david.webapi.handler.anotation.parameter.AnnotatedParameterResolver;
import me.david.webapi.handler.anotation.parameter.TransformerException;
import me.david.webapi.request.Request;

import java.lang.reflect.Parameter;

public class GetParameterResolver extends AnnotatedParameterResolver<Get, Object> {

    public GetParameterResolver() {
        super(Get.class);
    }

    @Override
    protected Object transformAnnotation(Get annotation, Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) {
        String strValue = request.getFirstGetParameter(annotation.value());
        Object value = LinkBase.getTransformerManager().transform(strValue, parameter.getType());

        if (value == null) throw new TransformerException("Invalid Data Type");
        return value;
    }
}
