package me.david.webapi.handler.anotation;

import lombok.*;
import me.david.davidlib.objects.Pair;
import me.david.webapi.WebApplication;
import me.david.webapi.handler.anotation.check.*;
import me.david.webapi.handler.anotation.handle.UseTransformer;
import me.david.webapi.handler.anotation.transform.Transformer;
import me.david.webapi.server.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@EqualsAndHashCode
@Getter
@Setter
public class AnnotationHandlerData {

    private String mapping = null;
    private int priority;
    private String method = null;
    private boolean costomMethod = false;
    private List<String> neededGet = new ArrayList<>(), neededPost = new ArrayList<>();
    private HashMap<String, String> getMustBe = new HashMap<>(), postMustBe = new HashMap<>();

    protected List<Transformer> costomTransformers = new ArrayList<>();

    public AnnotationHandlerData(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Mapping) {
                mapping = ((Mapping) annotation).path();
            } else if (annotation instanceof Priority) {
                priority = ((Priority) annotation).priority();
            } else if (annotation instanceof Last) {
                priority = Integer.MIN_VALUE;
            } else if (annotation instanceof First) {
                priority = Integer.MAX_VALUE;
            } else if (annotation instanceof NeedGetMethod) {
                method = "GET";
            } else if (annotation instanceof NeedPostMethod) {
                method = "POST";
            } else if (annotation instanceof NeedMethod) {
                method = ((NeedMethod) annotation).method().toUpperCase();
                costomMethod = true;
            } else if (annotation instanceof NeedGetParameter) {
                Collections.addAll(neededGet, ((NeedGetParameter) annotation).prameters());
            } else if (annotation instanceof NeedPostParameter) {
                Collections.addAll(neededPost, ((NeedPostParameter) annotation).prameters());
            } else if (annotation instanceof GetMustBe) {
                GetMustBe mustBeAnnotation = (GetMustBe) annotation;
                getMustBe.put(mustBeAnnotation.paramer(), mustBeAnnotation.value());
            } else if (annotation instanceof PostMustBe) {
                PostMustBe mustBeAnnotation = (PostMustBe) annotation;
                postMustBe.put(mustBeAnnotation.paramer(), mustBeAnnotation.value());
            } else if (annotation instanceof AddTransforwer) {
                for (Class<? extends Transformer> transformer : ((AddTransforwer) annotation).value()) {
                    try {
                        costomTransformers.add(transformer.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean valid(Request request) {
        if (mapping != null && request.getPath().matches(mapping)) return false;
        if (method != null && (costomMethod ? request.getMethod().getMethod().matches(method) : request.getMethod().getMethod().equals(method))) return false;

        for (String get : neededGet)
            if (!request.getGet().containsKey(get))
                return false;

        for (String get : neededPost)
            if (!request.getPost().containsKey(get))
                return false;

        for (Map.Entry<String, String> pair : getMustBe.entrySet()) {
            if (!request.getGet().containsKey(pair.getKey()) || !request.getGet().get(pair.getKey()).matches(pair.getValue()))
                return false;
        }

        for (Map.Entry<String, String> pair : postMustBe.entrySet()) {
            if (!request.getPost().containsKey(pair.getKey()) || !request.getPost().get(pair.getKey()).matches(pair.getValue()))
                return false;
        }

        return true;
    }

    @Getter
    public static class SupAnnotationHandlerData extends AnnotationHandlerData {

        private Method targetMethod;
        private List<Pair<Transformer, Parameter>> parameters = new ArrayList<>();

        public SupAnnotationHandlerData(Annotation[] annotations, Method method) {
            super(annotations);
            this.targetMethod = method;
            boolean found;
            for (Parameter parameter : method.getParameters()) {
                if (parameter.isAnnotationPresent(UseTransformer.class)) {
                    try {
                        parameters.add(new Pair<>(parameter.getAnnotation(UseTransformer.class).value().newInstance(), parameter));
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                found = false;
                for (Transformer transformer : costomTransformers) {
                    if (transformer.transformable(parameter)) {
                        parameters.add(new Pair<>(transformer, parameter));
                        found = true;
                        break;
                    }
                }
                if (found) continue;
                for (Transformer transformer : WebApplication.getInstance().getManager().getGlobalTransformer()) {
                    if (transformer.transformable(parameter)) {
                        parameters.add(new Pair<>(transformer, parameter));
                        break;
                    }
                }
            }
        }
    }

}
