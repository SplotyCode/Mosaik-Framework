package io.github.splotycode.mosaik.webapi.handler.anotation.parameter.defaultresolver;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.annotationbase.context.parameter.AnnotatedParameterResolver;
import io.github.splotycode.mosaik.annotationbase.context.exception.ParameterResolveException;
import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHandlerData;
import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.UrlParam;
import io.github.splotycode.mosaik.webapi.request.Request;

import java.lang.reflect.Parameter;

import static io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler.*;

public class UrlParamParameterResolver extends AnnotatedParameterResolver<UrlParam, Object, AnnotationHttpHandler> {

    public UrlParamParameterResolver() {
        super(UrlParam.class);
    }

    @Override
    protected Object transformAnnotation(AnnotationHttpHandler context, UrlParam annotation, Parameter parameter, DataFactory dataFactory) {
        Request request = dataFactory.getData(REQUEST);
        AnnotationHandlerData handler = dataFactory.getData(GLOBAL);
        AnnotationHandlerData.SupAnnotationHandlerData method = dataFactory.getData(SUP);

        if (handler.getMapping() == null && method.getMapping() == null) throw new ParameterResolveException("Could not find mapping");

        String methodResult = method.getMapping().match(request.getPath()).getVariables().get(annotation.value());
        if (methodResult == null) {
            if (annotation.needed()) throw new ParameterResolveException("Needed Parameter not present");
            return null;
        }

        Object value = context.parameterValue(method, parameter, methodResult);

        if (value == null) throw new ParameterResolveException("Invalid Data Type");
        return value;
    }
}
