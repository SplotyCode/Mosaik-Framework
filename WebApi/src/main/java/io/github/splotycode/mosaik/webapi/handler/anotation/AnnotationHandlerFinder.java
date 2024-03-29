package io.github.splotycode.mosaik.webapi.handler.anotation;

import io.github.splotycode.mosaik.runtime.Runtime;
import io.github.splotycode.mosaik.util.condition.ClassConditions;
import io.github.splotycode.mosaik.util.condition.Conditions;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.collector.ClassCollector;
import io.github.splotycode.mosaik.annotationbase.context.method.AddTransformer;
import io.github.splotycode.mosaik.util.reflection.classregister.ListClassRegister;
import io.github.splotycode.mosaik.webapi.config.WebConfig;
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

public class AnnotationHandlerFinder extends ListClassRegister<Object> implements HandlerFinder {

    private static Logger logger = Logger.getInstance(AnnotationHandlerFinder.class);

    private ClassCollector classCollector = ClassCollector.newInstance()
                                            .setNoDisable(true)
                                            .setOnlyClasses(true)
                                            .addCostom(Conditions.or(
                                                    item -> Arrays.stream(handlerAnnotation).anyMatch(item::isAnnotationPresent),
                                                    ClassConditions.anyMethod(method -> Arrays.stream(handlerAnnotation).anyMatch(method::isAnnotationPresent))
                                            ));


    @Getter private static Class<Annotation>[] handlerAnnotation = new Class[]{
            Handler.class, AddTransformer.class,
            GetMustBe.class, Mapping.class,
            NeedGetMethod.class, NeedGetParameter.class,
            NeedMethod.class, NeedPostMethod.class,
            NeedPostParameter.class, PostMustBe.class,
            ResponseContentType.class, NeedPermission.class
    };
    @Getter private AbstractWebServer webServer;

    public AnnotationHandlerFinder(AbstractWebServer webServer) {
        super(new ArrayList<>());
        this.webServer = webServer;
    }

    @Override
    public Collection<? extends HttpHandler> search() {
        List<AnnotationHttpHandler> handlers = new ArrayList<>();
        classCollector.setVisibility(webServer.getConfig().getData(WebConfig.SEARCH_ANNOTATION_HANDLERS_VISIBILITY));
        for (Class clazz : classCollector.collectAll(Runtime.getRuntime().getGlobalClassPath())) {
            handlers.add(new AnnotationHttpHandler(clazz, webServer));
        }
        for (Object obj : getAll()) {
            handlers.add(new AnnotationHttpHandler(obj, webServer));
        }
        return handlers;
    }

}
