package io.github.splotycode.mosaik.webapi.handler.anotation;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.condition.ClassConditions;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.reflection.annotation.MultiAnnotationContext;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.server.WebServer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AnnotationHttpHandler extends MultiAnnotationContext<AnnotationHttpHandler, AnnotationHandlerData> implements HttpHandler {

    private static final DataKey<List<AnnotationHandlerData>> SUPS = new DataKey<>("annotation.sups");
    public static final DataKey<Request> REQUEST = new DataKey<>("web.request");
    public static final DataKey<AnnotationHandlerData> GLOBAL = new DataKey<>("web.global");
    public static final DataKey<AnnotationHandlerData.SupAnnotationHandlerData> SUP = new DataKey<>("web.sup");

    private Collection<AnnotationHandler<AnnotationHttpHandler, ? extends Annotation, AnnotationHandlerData>> costom = new ArrayList<>();

    public void addCostomHandler(AnnotationHandler<AnnotationHttpHandler, ? extends Annotation, AnnotationHandlerData> handler) {
        costom.add(handler);
    }

    private WebServer webServer;

    public AnnotationHttpHandler(Class clazz, WebServer webServer) {
        this(webServer);
        feed(clazz);
    }

    public AnnotationHttpHandler(Object object, WebServer webServer) {
        this(webServer);
        feedObject(object);
    }

    private AnnotationHttpHandler(WebServer webServer) {
        if (webServer == null) throw new NullPointerException("webServer");
        this.webServer = webServer;
    }

    @Override
    public boolean valid(Request request) {
        if (data.valid(request)) {
            List<AnnotationHandlerData> subs = sub.stream()
                    .filter(sub -> {
                        boolean valid = sub.valid(request);
                        if (valid) {
                            String permission = sub.getNeedPermission() == null ? data.getNeedPermission() : sub.getNeedPermission();
                            return permission == null || request.hasPermission(permission);
                        }
                        return false;
                    })
                    .sorted(Comparator.comparingInt(AnnotationHandlerData::getPriority))
                    .collect(Collectors.toList());
            request.getDataFactory().putData(SUPS, subs);
            return !subs.isEmpty();
        }
        return false;
    }

    @Override
    public boolean handle(Request request) throws HandleRequestException {
        if (data.getLoadError() != null) {
            throw new HandleRequestException("Trying to work with crashed Handler: " + clazz.getSimpleName(), data.getLoadError());
        }
        data.applyCashingConfiguration(request.getResponse());
        data.applyContentType(request.getResponse());

        for (AnnotationHandlerData data : request.getDataFactory().getData(SUPS)) {
            AnnotationHandlerData.SupAnnotationHandlerData sup = (AnnotationHandlerData.SupAnnotationHandlerData) data;

            sup.applyCashingConfiguration(request.getResponse());
            sup.applyContentType(request.getResponse());
            try {
                DataFactory info = new DataFactory();
                info.putData(REQUEST, request);
                info.putData(GLOBAL, data);
                info.putData(SUP, sup);

                Object result = callmethod(sup, info);
                if (sup.isReturnContext()) {
                    request.getResponse().setContent((ResponseContent) result);
                } else if (result != null){
                    boolean cancel = (boolean) result;
                    if (cancel) return true;
                }
            } catch (Throwable ex) {
                throw new HandleRequestException("Could not invoke Method: " + sup.getMethod(), ex);
            }
        }
        return false;
    }

    @Override
    public int priority() {
        return data.getPriority();
    }

    @Override
    protected Collection<ParameterResolver> additionalParameterResolver() {
        return webServer.getParameterResolvers();
    }

    @Override
    public Class<? extends AnnotationHandlerData> elementClass() {
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
    public Object rawTransform(String input, Class<?> clazz, Collection<ValueTransformer> transformers) {
        return TransformerManager.getInstance().transform(input, clazz, transformers);
    }

    @Override
    public Collection<AnnotationHandler<AnnotationHttpHandler, ? extends Annotation, AnnotationHandlerData>> getAnnotationHandlers() {
        return costom;
    }

    @Override
    public String displayName() {
        return getClazz().getSimpleName();
    }
}
