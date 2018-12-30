package me.david.webapi.handler.anotation;

import lombok.Getter;
import me.david.davidlib.annotation.Disabled;
import me.david.davidlib.logger.Logger;
import me.david.davidlib.utils.reflection.ClassFinderHelper;
import me.david.webapi.handler.HandlerFinder;
import me.david.webapi.handler.HttpHandler;
import me.david.webapi.handler.anotation.check.*;
import me.david.webapi.server.AbstractWebServer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnnotationHandlerFinder implements HandlerFinder {

    private static Logger logger = Logger.getInstance(AnnotationHandlerFinder.class);

    @Getter private static Class<Annotation>[] handlerAnotation = new Class[]{
            Handler.class, AddTransformer.class,
            First.class, GetMustBe.class,
            Last.class, Mapping.class,
            NeedGetMethod.class, NeedGetParameter.class,
            NeedMethod.class, NeedPostMethod.class,
            NeedPostParameter.class, PostMustBe.class,
            Priority.class
    };
    private AbstractWebServer server;

    public AnnotationHandlerFinder(AbstractWebServer server) {
        this.server = server;
    }

    @Override
    public Collection<HttpHandler> search() {
        List<HttpHandler> handlers = new ArrayList<>();
        try {
            for (Class<?> clazz : ClassFinderHelper.getUserClasses()) {
                logger.debug("Inspecting: " + clazz.getName());
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
        logger.info("Found Annotation Handler: " + clazz.getSimpleName());
        Object obj = clazz.newInstance();
        AnnotationHandler handler = new AnnotationHandler(obj, server);
        handlers.add(handler);
    }

}
