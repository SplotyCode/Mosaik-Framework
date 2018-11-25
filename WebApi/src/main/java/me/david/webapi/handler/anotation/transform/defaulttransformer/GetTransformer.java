package me.david.webapi.handler.anotation.transform.defaulttransformer;

import me.david.webapi.handler.anotation.AnnotationHandlerData;
import me.david.webapi.handler.anotation.handle.Get;
import me.david.webapi.handler.anotation.transform.AnnotatedTransformer;
import me.david.webapi.request.Request;

import java.lang.reflect.Parameter;

public class GetTransformer extends AnnotatedTransformer<Get, String> {

    public GetTransformer() {
        super(Get.class);
    }

    @Override
    protected String transformAnnotation(Get annotation, Parameter parameter, Request request, AnnotationHandlerData handler, AnnotationHandlerData.SupAnnotationHandlerData method) {
        return request.getFirstGetParameter(annotation.value());
    }
}
