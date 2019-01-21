package io.github.splotycode.mosaik.webapi.handler.anotation;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.condition.ClassConditions;
import io.github.splotycode.mosaik.util.condition.Conditions;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.ClassCollector;
import io.github.splotycode.mosaik.webapi.handler.HandlerFinder;
import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.handler.anotation.check.*;
import io.github.splotycode.mosaik.webapi.server.AbstractWebServer;
import lombok.Getter;

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
                Object obj = clazz.newInstance();
                AnnotationHandler handler = new AnnotationHandler(obj, server);
                handlers.add(handler);
            }
        } catch (IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
        }
        logger.info("Found Annotation Handlers (" + handlers.size() + "/" + classCollector.totalResults() + "): " + StringUtil.join(handlers, handler -> handler.getClass().getSimpleName(), ", "));
        return handlers;
    }

}
