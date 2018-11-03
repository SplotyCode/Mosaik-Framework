package me.david.webapi.handler.anotation;

import me.david.davidlib.objects.Pair;
import me.david.webapi.handler.HttpHandler;
import me.david.webapi.handler.anotation.transform.Transformer;
import me.david.webapi.handler.anotation.transform.TransformerException;
import me.david.webapi.server.HandleRequestException;
import me.david.webapi.server.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    public boolean handle(Request request) throws HandleRequestException {
        for (AnnotationHandlerData.SupAnnotationHandlerData sup : subs.stream().filter(sub -> sub.valid(request)).sorted(Comparator.comparingInt(AnnotationHandlerData::getPriority)).collect(Collectors.toList())) {
            Object[] objects = new Object[sup.getParameters().size()];
            int i = 0;
            for (Pair<Transformer, Parameter> pair : sup.getParameters()) {
                try {
                    objects[i] = pair.getOne().transform(pair.getTwo(), request);
                } catch (TransformerException ex) {
                    throw new HandleRequestException("Failed to transform parameters", ex);
                }
                i++;
            }
            try {
                boolean cancel = (boolean) sup.getTargetMethod().invoke(handlerObj, objects);
                if (cancel) return true;
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
