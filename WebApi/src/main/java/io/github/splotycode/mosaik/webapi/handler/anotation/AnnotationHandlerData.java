package io.github.splotycode.mosaik.webapi.handler.anotation;

import io.github.splotycode.mosaik.annotations.AnnotationHelper;
import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.util.reflection.annotation.AnnotationData;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.UseResolver;
import io.github.splotycode.mosaik.webapi.handler.UrlPattern;
import io.github.splotycode.mosaik.webapi.handler.anotation.check.*;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.cache.Cache;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.cache.CacheDefaultProvider;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.request.RequestHeader;
import io.github.splotycode.mosaik.webapi.response.HttpCashingConfiguration;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.server.AbstractWebServer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@EqualsAndHashCode
@Getter
@Setter
public class AnnotationHandlerData extends AnnotationData {

    private UrlPattern mapping = null;
    private int priority;
    private String method = null;
    private boolean costomMethod = false;
    private String host;
    private List<String> neededGet = new ArrayList<>(), neededPost = new ArrayList<>();
    private HashMap<String, String> getMustBe = new HashMap<>(), postMustBe = new HashMap<>();
    private HttpCashingConfiguration cashingConfiguration;

    protected List<ParameterResolver> costomParameterResolvers = new ArrayList<>();

    @Override
    public void buildData(Annotation[] annotations) {
        priority = AnnotationHelper.getPriority(annotations);
        for (Annotation annotation : annotations) {
            if (annotation instanceof Mapping) {
                mapping = new UrlPattern(((Mapping) annotation).value());
            } else if (annotation instanceof NeedGetMethod) {
                setMethod("GET");
            } else if (annotation instanceof NeedPostMethod) {
                setMethod("POST");
            } else if (annotation instanceof NeedMethod) {
                setMethod(((NeedMethod) annotation).method().toUpperCase());
                costomMethod = true;
            } else if (annotation instanceof NeedGetParameter) {
                setMethod("GET");
                Collections.addAll(neededGet, ((NeedGetParameter) annotation).parameters());
            } else if (annotation instanceof NeedPostParameter) {
                setMethod("POST");
                Collections.addAll(neededPost, ((NeedPostParameter) annotation).parameters());
            } else if (annotation instanceof GetMustBe) {
                setMethod("GET");
                GetMustBe mustBeAnnotation = (GetMustBe) annotation;
                getMustBe.put(mustBeAnnotation.parameter(), mustBeAnnotation.value());
            } else if (annotation instanceof PostMustBe) {
                setMethod("POST");
                PostMustBe mustBeAnnotation = (PostMustBe) annotation;
                postMustBe.put(mustBeAnnotation.parameter(), mustBeAnnotation.value());
            } else if (annotation instanceof Host) {
                host = (((Host) annotation).value()).trim().toLowerCase(Locale.ENGLISH);
            } else if (annotation instanceof CacheDefaultProvider) {
                cashingConfiguration = ((CacheDefaultProvider) annotation).value().get();
            } else if (annotation instanceof Cache) {
                Cache cache = (Cache) annotation;
                cashingConfiguration = new HttpCashingConfiguration(
                        cache.expires(),
                        cache.noCache(),
                        cache.noStore(),
                        cache.noTransform(),
                        cache.onlyIfCashed(),
                        cache.mustRevalidate(),
                        cache.isPublic(),
                        cache.isPrivate(),
                        cache.maxAge(),
                        cache.maxStale(),
                        cache.minFresh(),
                        CollectionUtil.newHashSet(cache.modes()),
                        cache.eTagMode()
                );
            }
        }
    }

    private void setMethod(String method) {
        if (this.method != null && !method.equals(this.method))
            throw new IllegalStateException("Can not force two different methods (" + method + " and " + this.method);
        this.method = method;
    }

    void applyCashingConfiguration(Response response) {
        try {
            if (cashingConfiguration != null) {
                response.applyCashingConfiguration(cashingConfiguration);
            }
        } catch (Throwable throwable) {
            throw new HandleRequestException("Failed to apply cashing configuration");
        }
    }

    public boolean valid(Request request) {
        String host = request.getHeader(RequestHeader.HOST);
        if (host == null) {
            if (this.host != null) return false;
        } else if (!host.trim().toLowerCase(Locale.ENGLISH).equals(host)) {
             return false;
        }
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
            if (!request.getPost().containsKey(pair.getKey()) || !request.getFirstPostParameter(pair.getKey()).equals(pair.getValue()))
                return false;
        }
        return true;
    }

    @Getter
    public static class SupAnnotationHandlerData extends AnnotationHandlerData {

        private Method targetMethod;
        private List<Pair<ParameterResolver, Parameter>> parameters = new ArrayList<>();
        private boolean returnContext;
        private String displayName;

        public void postHandle(AbstractWebServer server, AnnotationHandlerData global) {
            try {
                Method method = (Method) element;
                displayName = method.getDeclaringClass().getSimpleName() + "#" + method.getName();
                this.targetMethod = method;
                boolean found;
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.isAnnotationPresent(UseResolver.class)) {
                        parameters.add(new Pair<>(parameter.getAnnotation(UseResolver.class).value().newInstance(), parameter));
                        break;
                    }
                    found = false;
                    for (ParameterResolver parameterResolver : costomParameterResolvers) {
                        if (parameterResolver.transformable(parameter)) {
                            parameters.add(new Pair<>(parameterResolver, parameter));
                            found = true;
                            break;
                        }
                    }
                    for (ParameterResolver parameterResolver : global.costomParameterResolvers) {
                        if (parameterResolver.transformable(parameter)) {
                            parameters.add(new Pair<>(parameterResolver, parameter));
                            found = true;
                            break;
                        }
                    }
                    if (found) continue;
                    for (ParameterResolver parameterResolver : server.getParameterResolvers()) {
                        if (parameterResolver.transformable(parameter)) {
                            parameters.add(new Pair<>(parameterResolver, parameter));
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
                loadError = ex;
            } catch (Throwable ex) {
                loadError = new IllegalHandlerException("Exception while parsing handler", ex);
            }
        }
    }

}
