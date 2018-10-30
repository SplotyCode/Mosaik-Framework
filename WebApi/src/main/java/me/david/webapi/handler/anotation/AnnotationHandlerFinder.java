package me.david.webapi.handler.anotation;

import com.google.common.reflect.ClassPath;
import lombok.Getter;
import me.david.webapi.WebApplication;
import me.david.webapi.handler.HandlerFinder;
import me.david.webapi.handler.HttpHandler;
import me.david.webapi.handler.anotation.check.Handler;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnnotationHandlerFinder implements HandlerFinder {

    @Getter private static Class<Annotation>[] handlerAnotation = new Class[]{Handler.class};

    @Override
    public Collection<HttpHandler> search() {
        List<HttpHandler> handlers = new ArrayList<>();
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(WebApplication.getInstance().getClass().getClassLoader()).getAllClasses()) {
                Class<?> clazz = classInfo.load();
                for (Class<Annotation> annotation : handlerAnotation) {
                    if (clazz.isAnnotationPresent(annotation)) {
                        add(clazz, handlers);
                        continue;
                    }
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(annotation)) {
                            add(clazz, handlers);
                            break;
                        }
                    }
                }
            }
        } catch (IOException | IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return handlers;
    }

    private void add(Class<?> clazz, List<HttpHandler> handlers) throws IllegalAccessException, InstantiationException {
        Object obj = clazz.newInstance();
        AnnotationHandler handler = new AnnotationHandler(obj);
        handlers.add(handler);
    }

}
