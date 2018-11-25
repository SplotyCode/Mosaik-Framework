package me.david.webapi.handler.anotation;

import me.david.davidlib.helper.Pair;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.handler.HttpHandler;
import me.david.webapi.handler.anotation.parameter.ParameterResolver;
import me.david.webapi.handler.anotation.parameter.ParameterResolveException;
import me.david.webapi.response.content.ResponseContent;
import me.david.webapi.server.HandleRequestException;
import me.david.webapi.request.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotationHandler implements HttpHandler {

    private Object handlerObj;
    private AnnotationHandlerData global;
    private List<AnnotationHandlerData.SupAnnotationHandlerData> subs = new ArrayList<>();

    public AnnotationHandler(Object handlerObj, HandlerManager manager) {
        this.handlerObj = handlerObj;
        global = new AnnotationHandlerData(handlerObj.getClass().getAnnotations());

        for (Method method : handlerObj.getClass().getMethods()) {
            for (Class<Annotation> annotation : AnnotationHandlerFinder.getHandlerAnotation()) {
                if (method.isAnnotationPresent(annotation)) {
                    AnnotationHandlerData.SupAnnotationHandlerData data = new AnnotationHandlerData.SupAnnotationHandlerData(method.getDeclaredAnnotations(), method, manager);
                    subs.add(data);
                    break;
                }
            }
        }
    }

    @Override
    public boolean valid(Request request) {
        return global.valid(request) || subs.stream().anyMatch(sub -> sub.valid(request));
    }

    @Override
    public boolean handle(Request request) throws HandleRequestException {
        if (global.getLoadingError() != null) {
            throw new HandleRequestException("Trying to work with crashed Handler: " + handlerObj.getClass().getSimpleName(), global.getLoadingError());
        }
        for (AnnotationHandlerData.SupAnnotationHandlerData sup : subs.stream().filter(sub -> sub.valid(request)).sorted(Comparator.comparingInt(AnnotationHandlerData::getPriority)).collect(Collectors.toList())) {
            if (sup.getLoadingError() != null) {
                throw new HandleRequestException("Count not use Handler Method: " + sup.getDisplayName() + "because it fails loading on startup", sup.getLoadingError());
            }
            Object[] objects = new Object[sup.getParameters().size()];
            int i = 0;
            for (Pair<ParameterResolver, Parameter> pair : sup.getParameters()) {
                try {
                    objects[i] = pair.getOne().transform(pair.getTwo(), request, global, sup);
                } catch (ParameterResolveException ex) {
                    throw new HandleRequestException("Failed to transform parameters", ex);
                }
                i++;
            }
            try {
                Object result = sup.getTargetMethod().invoke(handlerObj, objects);
                if (sup.isReturnContext()) {
                    request.getResponse().setContent((ResponseContent) result);
                } else if (result != null){
                    boolean cancel = (boolean) result;
                    if (cancel) return true;
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new HandleRequestException("Could not invoke Method: " + sup.getTargetMethod().getName(), ex);
            }
        }
        return false;
    }

    @Override
    public int priority() {
        return global.getPriority();
    }
}
