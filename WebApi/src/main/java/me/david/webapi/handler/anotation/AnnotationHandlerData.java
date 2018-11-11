package me.david.webapi.handler.anotation;

import lombok.*;
import me.david.davidlib.helper.Pair;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.handler.UrlPattern;
import me.david.webapi.handler.anotation.check.*;
import me.david.webapi.handler.anotation.handle.UseTransformer;
import me.david.webapi.handler.anotation.transform.Transformer;
import me.david.webapi.response.content.ResponseContent;
import me.david.webapi.server.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

@EqualsAndHashCode
@Getter
@Setter
public class AnnotationHandlerData {

    private UrlPattern mapping = null;
    private int priority;
    private String method = null;
    private boolean costomMethod = false;
    private List<String> neededGet = new ArrayList<>(), neededPost = new ArrayList<>();
    private HashMap<String, String> getMustBe = new HashMap<>(), postMustBe = new HashMap<>();
    private Throwable loadingError = null;

    protected List<Transformer> costomTransformers = new ArrayList<>();

    public AnnotationHandlerData(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Mapping) {
                mapping = new UrlPattern(((Mapping) annotation).value());
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
                        loadingError = e;
                    }
                }
            }
        }
    }

    public boolean valid(Request request) {
        if (mapping != null && !mapping.match(request.getPath()).isMatch()) return false;
        if (method != null && (costomMethod ? request.getMethod().getMethod().matches(method) : request.getMethod().getMethod().equals(method))) return false;
        for (String get : neededGet)
            if (!request.getGet().containsKey(get))
                return false;

        for (String get : neededPost)
            if (!request.getPost().containsKey(get))
                return false;

        for (Map.Entry<String, String> pair : getMustBe.entrySet()) {
            if (!request.getGet().containsKey(pair.getKey()) || !request.getFirstGetParameter(pair.getKey()).equals(pair.getValue()))
                return false;
        }

        for (Map.Entry<String, String> pair : postMustBe.entrySet()) {
            if (!request.getPost().containsKey(pair.getKey()) || !request.getPost().get(pair.getKey()).equals(pair.getValue()))
                return false;
        }
        return true;
    }

    @Getter
    public static class SupAnnotationHandlerData extends AnnotationHandlerData {

        private Method targetMethod;
        private List<Pair<Transformer, Parameter>> parameters = new ArrayList<>();
        private boolean returnContext;
        private String displayName;

        public SupAnnotationHandlerData(Annotation[] annotations, Method method, HandlerManager handler) {
            super(annotations);
            try {
                displayName = method.getDeclaringClass().getSimpleName() + "#" + method.getName();
                this.targetMethod = method;
                if (Modifier.isAbstract(method.getModifiers()))
                    throw new IllegalHandlerException("Handler might not be abstract" + method.getDeclaringClass().getSimpleName() + "#" + method.getName());
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
                    for (Transformer transformer : handler.getGlobalTransformer()) {
                        if (transformer.transformable(parameter)) {
                            parameters.add(new Pair<>(transformer, parameter));
                            found = true;
                            break;
                        }
                    }
                    if (!found) throw new IllegalHandlerException("Could not find transformer for " + parameter.getName() + " in " + displayName);
                }
                Class<?> returnType = method.getReturnType();
                returnContext = ResponseContent.class.isAssignableFrom(returnType);
                if (!returnContext && returnType != boolean.class &&
                        returnType != Boolean.class &&
                        returnType != void.class) {
                    throw new IllegalHandlerException("Invalid method type of handler " + displayName);
                }
            } catch (IllegalHandlerException ex) {
                setLoadingError(ex);
            } catch (Exception ex) {
                setLoadingError(new IllegalHandlerException("Exception while parsing handler", ex));
            }

        }
    }

}
