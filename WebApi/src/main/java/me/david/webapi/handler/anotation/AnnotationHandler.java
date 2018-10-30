package me.david.webapi.handler.anotation;

import me.david.webapi.handler.HttpHandler;
import me.david.webapi.server.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotationHandler implements HttpHandler {

    private Object handlerObj;
    private AnnotationHandlerData global;
    private List<AnnotationHandlerData.SupAnnotationHandlerData> subs = new ArrayList<>();

    public AnnotationHandler(Object handlerObj) {
        this.handlerObj = handlerObj;
        global = new AnnotationHandlerData(handlerObj.getClass().getAnnotations());

        for (Method method : handlerObj.getClass().getMethods()) {
            for (Class<Annotation> annotation : AnnotationHandlerFinder.getHandlerAnotation()) {
                if (method.isAnnotationPresent(annotation)) {
                    subs.add(new AnnotationHandlerData.SupAnnotationHandlerData(method.getDeclaredAnnotations(), method));
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
    public boolean handle(Request request) {
        for (AnnotationHandlerData.SupAnnotationHandlerData sup : subs.stream().filter(sub -> sub.valid(request)).sorted(Comparator.comparingInt(AnnotationHandlerData::getPriority)).collect(Collectors.toList())) {
            try {
                boolean cancel = (boolean) sup.getMethod().invoke(handlerObj, request);
                if (cancel) return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
