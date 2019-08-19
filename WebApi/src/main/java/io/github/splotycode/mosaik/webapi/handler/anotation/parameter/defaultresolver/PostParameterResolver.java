package io.github.splotycode.mosaik.webapi.handler.anotation.parameter.defaultresolver;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.AnnotatedParameterResolver;
import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHttpHandler;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.Post;
import io.github.splotycode.mosaik.webapi.request.Request;

import java.lang.reflect.Parameter;

public class PostParameterResolver extends AnnotatedParameterResolver<Post, Object, AnnotationHttpHandler> {

    public PostParameterResolver() {
        super(Post.class);
    }

    @Override
    protected Object transformAnnotation(AnnotationHttpHandler context, Post annotation, Parameter parameter, DataFactory dataFactory) {
        Request request = dataFactory.getData(AnnotationHttpHandler.REQUEST);
        String strValue = request.getFirstPostParameter(annotation.value());
        return context.parameterValue(dataFactory.getData(AnnotationHttpHandler.SUP), parameter, strValue);
    }
}
