package io.github.splotycode.mosaik.webapi.handler.anotation;

import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.condition.ClassConditions;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.reflection.annotation.MultiAnnotationContext;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.ParameterResolveException;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.server.AbstractWebServer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AnnotationHttpHandler extends MultiAnnotationContext<AnnotationHttpHandler, AnnotationHandlerData> implements HttpHandler {

    public static DataKey<Request> REQUEST = new DataKey<>("request");
    public static DataKey<AnnotationHandlerData> GLOBAL = new DataKey<>("global");
    public static DataKey<AnnotationHandlerData.SupAnnotationHandlerData> SUP = new DataKey<>("sup");

    private Collection<AnnotationHandler<AnnotationHttpHandler, Annotation, AnnotationHandlerData>> costom = new ArrayList<>();

    public void addCostomHandler(AnnotationHandler<AnnotationHttpHandler, Annotation, AnnotationHandlerData> handler) {
        costom.add(handler);
    }

    public AnnotationHttpHandler(Class clazz, AbstractWebServer server) {
        feed(clazz);
        sub.forEach(data -> ((AnnotationHandlerData.SupAnnotationHandlerData)data).postHandle(server, global));
    }

    public AnnotationHttpHandler(Object object, AbstractWebServer server) {
        feedObject(object);
        sub.forEach(data -> ((AnnotationHandlerData.SupAnnotationHandlerData)data).postHandle(server, global));
    }

    @Override
    public boolean valid(Request request) {
        return global.valid(request) && sub.stream().anyMatch(sub -> sub.valid(request));
    }

    @Override
    public boolean handle(Request request) throws HandleRequestException {
        if (global.getLoadError() != null) {
            throw new HandleRequestException("Trying to work with crashed Handler: " + clazz.getSimpleName(), global.getLoadError());
        }
        global.applyCashingConfiguration(request.getResponse());

        for (AnnotationHandlerData data : sub.stream().filter(sub -> sub.valid(request)).sorted(Comparator.comparingInt(AnnotationHandlerData::getPriority)).collect(Collectors.toList())) {
            AnnotationHandlerData.SupAnnotationHandlerData sup = (AnnotationHandlerData.SupAnnotationHandlerData) data;
            if (sup.getLoadError() != null) {
                throw new HandleRequestException("Count not use Handler Method: " + sup.getDisplayName() + " because it fails loading on startup", sup.getLoadError());
            }
            Object[] objects = new Object[sup.getParameters().size()];
            try {
                int i = 0;
                for (Pair<ParameterResolver, Parameter> pair : sup.getParameters()) {
                    try {
                        DataFactory dataFactory = new DataFactory();
                        dataFactory.putData(REQUEST, request);
                        dataFactory.putData(GLOBAL, global);
                        dataFactory.putData(SUP, sup);
                        objects[i] = pair.getOne().transform(pair.getTwo(), dataFactory);
                    } catch (ParameterResolveException ex) {
                        throw new HandleRequestException("Failed to transform parameter", ex);
                    }
                    i++;
                }
            } catch (Throwable ex) {
                throw new HandleRequestException("Could not prepare parameters for Method: " + sup.getTargetMethod().getName(), ex);
            }
            sup.applyCashingConfiguration(request.getResponse());
            try {
                Object result = sup.getTargetMethod().invoke(object, objects);
                if (sup.isReturnContext()) {
                    request.getResponse().setContent((ResponseContent) result);
                } else if (result != null){
                    boolean cancel = (boolean) result;
                    if (cancel) return true;
                }
            } catch (Throwable ex) {
                throw new HandleRequestException("Could not invoke Method: " + sup.getTargetMethod().getName(), ex);
            }
        }
        return false;
    }

    @Override
    public int priority() {
        return global.getPriority();
    }

    @Override
    protected Class<? extends AnnotationHandlerData> globalDataClass() {
        return AnnotationHandlerData.class;
    }

    @Override
    protected Predicate<AnnotatedElement> methodPredicate() {
        return ClassConditions.needOneAnnotation(AnnotationHandlerFinder.getHandlerAnnotation());
    }

    @Override
    protected Class<? extends AnnotationHandlerData.SupAnnotationHandlerData> methodDataClass() {
        return AnnotationHandlerData.SupAnnotationHandlerData.class;
    }

    @Override
    public AnnotationHttpHandler self() {
        return this;
    }

    @Override
    public Collection<AnnotationHandler<AnnotationHttpHandler, Annotation, AnnotationHandlerData>> getAnnotationHandlers() {
        return costom;
    }
}
