package me.david.webapi.handler.anotation;

import lombok.Getter;
import me.david.davidlib.annotation.Disabled;
import me.david.davidlib.utils.ClassFinderHelper;
import me.david.webapi.handler.HandlerFinder;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.handler.HttpHandler;
import me.david.webapi.handler.anotation.check.Handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnnotationHandlerFinder implements HandlerFinder {

    @Getter private static Class<Annotation>[] handlerAnotation = new Class[]{Handler.class};
    private HandlerManager handlerManager;

    public AnnotationHandlerFinder(HandlerManager handlerManager) {
        this.handlerManager = handlerManager;
    }

    @Override
    public Collection<HttpHandler> search() {
        List<HttpHandler> handlers = new ArrayList<>();
        try {
            for (Class<?> clazz : ClassFinderHelper.getUserClasses()) {
                System.out.println("Inspecting: " + clazz.getName());
                for (Class<Annotation> annotation : handlerAnotation) {
                    if (clazz.isAnnotationPresent(Disabled.class)) {
                        break;
                    }
                    if (clazz.isAnnotationPresent(annotation)) {
                        add(clazz, handlers);
                        break;
                    }
                    boolean cancel = false;
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(annotation)) {
                            add(clazz, handlers);
                            cancel = true;
                            break;
                        }
                    }
                    if (cancel) break;
                }
            }
        } catch (IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return handlers;
    }

    private void add(Class<?> clazz, List<HttpHandler> handlers) throws IllegalAccessException, InstantiationException {
        System.out.println("Added " + clazz.getSimpleName());
        Object obj = clazz.newInstance();
        AnnotationHandler handler = new AnnotationHandler(obj, handlerManager);
        handlers.add(handler);
    }

}
