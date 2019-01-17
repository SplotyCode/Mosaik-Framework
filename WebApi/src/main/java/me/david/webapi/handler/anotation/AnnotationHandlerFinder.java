package me.david.webapi.handler.anotation;

import lombok.Getter;
import me.david.davidlib.util.condition.ClassConditions;
import me.david.davidlib.util.condition.Conditions;
import me.david.davidlib.util.logger.Logger;
import me.david.davidlib.util.reflection.ClassCollector;
import me.david.webapi.handler.HandlerFinder;
import me.david.webapi.handler.HttpHandler;
import me.david.webapi.handler.anotation.check.*;
import me.david.webapi.server.AbstractWebServer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AnnotationHandlerFinder implements HandlerFinder {

    private static Logger logger = Logger.getInstance(AnnotationHandlerFinder.class);

    private ClassCollector classCollector = ClassCollector.newInstance()
                                            .setNoDisableds(true)
                                            .setOnlyClasses(true)
                                            .addCostom(Conditions.or(
                                                    item -> Arrays.stream(handlerAnotation).anyMatch(item::isAnnotationPresent),
                                                    ClassConditions.anyMethod(method -> Arrays.stream(handlerAnotation).anyMatch(method::isAnnotationPresent))
                                            ));


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
            for (Class<?> clazz : classCollector.collectAll()) {
                logger.info("Found Annotation Handler: " + clazz.getSimpleName());
                Object obj = clazz.newInstance();
                AnnotationHandler handler = new AnnotationHandler(obj, server);
                handlers.add(handler);
            }
        } catch (IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return handlers;
    }

}
